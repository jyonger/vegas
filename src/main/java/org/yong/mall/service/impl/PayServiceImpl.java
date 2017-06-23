package org.yong.mall.service.impl;

import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.xml.internal.rngom.parse.host.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yong.mall.dao.OrderInfoMapper;
import org.yong.mall.dao.OrderItemMapper;
import org.yong.mall.dao.PayInfoMapper;
import org.yong.mall.dict.PayDict;
import org.yong.mall.dto.BaseResponse;
import org.yong.mall.enums.OrderStatusEnum;
import org.yong.mall.enums.PayPlatformEnum;
import org.yong.mall.enums.ResultEnum;
import org.yong.mall.pojo.OrderInfo;
import org.yong.mall.pojo.OrderItem;
import org.yong.mall.pojo.PayInfo;
import org.yong.mall.service.IPayService;
import org.yong.mall.util.BigDecimalUtil;
import org.yong.mall.util.DateTimeUtil;
import org.yong.mall.util.FTPUtil;
import org.yong.mall.util.PropertiesUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yong on 2017/6/19.
 */
@Service("iPayService")
public class PayServiceImpl implements IPayService {

    private static final Logger LOG = LoggerFactory.getLogger(PayServiceImpl.class);

    private static AlipayTradeService tradeService;

    static {
        /**
         * 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         * Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /**
         * 使用Configs提供的默认参数 AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }

    @Autowired
    OrderInfoMapper orderMapper;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    PayInfoMapper payInfoMapper;

    @Override
    public BaseResponse aliPay(Integer userId, Long orderNo, String path) {
        HashMap<String, String> resultMap = Maps.newHashMap();
        OrderInfo orderInfo = orderMapper.getByUserIdAndOrderNo(userId, orderNo);
        if (orderInfo == null) {
            return BaseResponse.getErrorMessage("此用户无该订单");
        }
        resultMap.put("orderNo", orderInfo.getOrderNo().toString());
        List<OrderItem> items = orderItemMapper.listByUserIdAndOrderNo(userId, orderNo);

        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = orderInfo.getOrderNo().toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = items.get(0).getGoodsName();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = orderInfo.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = items.get(0).getGoodsName();
        if (items.size() > 0) {
            body += "等";
        }

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        for (OrderItem item : items) {
            GoodsDetail detail = GoodsDetail.newInstance(item.getGoodsId().toString(), item.getGoodsName(),
                    BigDecimalUtil.mul
                            (item.getCurrentUnitPrice().doubleValue(), new Double(100)).longValue(), item
                            .getQuantity());
            goodsDetailList.add(detail);
        }

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder().setSubject(subject)
                .setTotalAmount(totalAmount).setOutTradeNo(outTradeNo).setUndiscountableAmount(undiscountableAmount)
                .setSellerId(sellerId).setBody(body).setOperatorId(operatorId).setStoreId(storeId)
                .setExtendParams(extendParams).setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(PropertiesUtil.getProperty("ali.pay.callback.http"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                LOG.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();

                File folder = new File(path);
                if (!folder.exists()) {
                    folder.setWritable(true);
                    folder.mkdirs();
                }
                String qrPath = String.format(path + "/qr-%s.png", response.getOutTradeNo());
                String qrFilename = String.format("qr-%s.png", response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);

                // 将生成的二维码上传到FTP服务器上
                File targetFile = new File(qrPath);
                try {
                    FTPUtil.saveFile(Lists.newArrayList(targetFile));
                } catch (IOException e) {
                    LOG.error("上传二维码到FTP出现错误", e);
                }

                String qrUrl = PropertiesUtil.getProperty("ftp.server.http.prefix") + qrFilename;
                resultMap.put("qrUrl", qrUrl);
                return new BaseResponse(ResultEnum.SUCCESS.getCode(), resultMap);
            case FAILED:
                LOG.error("支付宝预下单失败!!!");
                return BaseResponse.getErrorMessage("支付宝预下单失败!!!");

            case UNKNOWN:
                LOG.error("系统异常，预下单状态未知!!!");
                return BaseResponse.getErrorMessage("系统异常，预下单状态未知!!!");

            default:
                LOG.error("不支持的交易状态，交易返回异常!!!");
                return BaseResponse.getErrorMessage("不支持的交易状态，交易返回异常!!!");
        }
    }

    @Override
    public BaseResponse aliCallback(HashMap<String, String> params) {
        String outTradeNo = params.get("out_trade_no");
        String tradeNo = params.get("trade_no");
        String tradeStatus = params.get("trade_status");

        OrderInfo orderInfo = orderMapper.getByOrderNo(Long.valueOf(outTradeNo));
        if (orderInfo == null) {
            return BaseResponse.getErrorMessage("非系统订单");
        }

        if (orderInfo.getStatus() >= OrderStatusEnum.PAID.getCode()) {
            return BaseResponse.getErrorMessage("此订单已支付");
        }

        if (PayDict.AliPayStatus.TRADE_SUCCESS.equals(tradeStatus)) {
            orderInfo.setStatus(OrderStatusEnum.PAID.getCode());
            orderInfo.setPaymentType(PayPlatformEnum.ALIPAY.getCode());
            orderInfo.setPaymentTime(DateTimeUtil.toDate(params.get("gmt_payment")));
            orderMapper.updateByPrimaryKey(orderInfo);
        }

        // 创建支付信息
        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(orderInfo.getUserId());
        payInfo.setOrderNo(orderInfo.getOrderNo());
        // true: 支付宝 false: 微信
        payInfo.setPlatform(true);
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);
        payInfoMapper.insert(payInfo);

        return BaseResponse.getSuccessMessage("支付成功");
    }
}
