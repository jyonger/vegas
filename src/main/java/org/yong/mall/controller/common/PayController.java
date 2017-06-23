package org.yong.mall.controller.common;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yong.mall.dict.PayDict;
import org.yong.mall.dict.UserDict;
import org.yong.mall.dto.BaseResponse;
import org.yong.mall.enums.ResultEnum;
import org.yong.mall.pojo.UserInfo;
import org.yong.mall.service.IPayService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Yong on 2017/6/19.
 */
@Controller
public class PayController {

    private static final Logger LOG = LoggerFactory.getLogger(PayController.class);

    @Autowired
    IPayService payService;

    @RequestMapping(value = "/user/order/pay", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse pay(HttpSession session, Long orderNo) {
        UserInfo userInfo = (UserInfo) session.getAttribute(UserDict.CURRENT_USER);
        if (userInfo == null) {
            return BaseResponse.getErrorMessage(ResultEnum.NEED_LOGIN.getDesc());
        }

        String path = session.getServletContext().getRealPath("upload");
        return payService.aliPay(userInfo.getId(), orderNo, path);
    }

    @RequestMapping(value = "/pay/ali/callback")
    @ResponseBody
    public Object aliCallback(HttpServletRequest request) {
        HashMap<String, String> params = Maps.newHashMap();
        Map requestParams = request.getParameterMap();
        for (Iterator iterator = requestParams.keySet().iterator(); iterator.hasNext(); ) {
            String key = (String) iterator.next();
            String[] values = (String[]) requestParams.get(key);
            StringBuilder value = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                value.append(values[i] + ",");
            }
            value.setLength(value.length() - 1);
            params.put(key, value.toString());
        }

        LOG.info("支付宝回调,sign:{},trade_status:{},参数:{}", params.get("sign"), params.get("trade_status"), params
                .toString());

        params.remove("sign_type");
        try {

            boolean rsaCheck = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8",
                    Configs.getSignType());
            if (!rsaCheck) {
                return BaseResponse.getErrorMessage("非支付宝回调");
            }
        } catch (AlipayApiException e) {
            LOG.error("验证回调参数出现错误", e);
        }

        BaseResponse baseResponse = payService.aliCallback(params);
        if (baseResponse.isSuccess()) {
            return PayDict.AliPayStatus.RESPONSE_SUCCESS;
        }
        return PayDict.AliPayStatus.RESPONSE_FAILED;
    }
}
