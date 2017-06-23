package org.yong.mall.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.yong.mall.dao.*;
import org.yong.mall.dict.GoodsDict;
import org.yong.mall.dto.BaseResponse;
import org.yong.mall.enums.OrderStatusEnum;
import org.yong.mall.enums.PayPlatformEnum;
import org.yong.mall.enums.ResultEnum;
import org.yong.mall.pojo.*;
import org.yong.mall.util.BigDecimalUtil;
import org.yong.mall.util.DateTimeUtil;
import org.yong.mall.util.PropertiesUtil;
import org.yong.mall.vo.AddressVo;
import org.yong.mall.vo.OrderGoodsVo;
import org.yong.mall.vo.OrderItemVo;
import org.yong.mall.vo.OrderVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Yong on 2017/6/19.
 */
@Service("iOrderService")
public class OrderService implements IOrderService {

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    UserCartMapper cartMapper;

    @Autowired
    GoodsInfoMapper goodsMapper;

    @Autowired
    UserAddressMapper addressMapper;

    @Override
    public BaseResponse getPayStatusByUserIdAndOrderNo(Integer userId, Long orderNo) {
        OrderInfo orderInfo = orderInfoMapper.getByUserIdAndOrderNo(userId, orderNo);
        if (orderInfo == null) {
            return BaseResponse.getErrorMessage("此用户不存在该订单");
        }

        if (orderInfo.getStatus() >= OrderStatusEnum.PAID.getCode()) {
            return new BaseResponse(ResultEnum.SUCCESS.getCode(), true);
        }
        return new BaseResponse(ResultEnum.SUCCESS.getCode(), false);
    }

    @Override
    public BaseResponse cartCheckout(Integer userId, Integer addressId) {
        List<UserCart> carts = cartMapper.getCheckedByUserId(userId);
        if (CollectionUtils.isEmpty(carts)) {
            return BaseResponse.getErrorMessage("未选择购物车中的商品");
        }

        BaseResponse baseResponse = assembleOrderItem(userId, carts);
        if (!baseResponse.isSuccess()) {
            return baseResponse;
        }
        List<OrderItem> items = (List<OrderItem>) baseResponse.getData();


        // 生成订单
        BigDecimal payment = sumCartTotal(items);
        OrderInfo order = assembleOrder(userId, addressId, payment);
        if (order == null) {
            return BaseResponse.getErrorMessage("系统生成订单失败");
        }
        for (OrderItem item : items) {
            item.setOrderNo(order.getOrderNo());
        }

        orderItemMapper.batchInsert(items);

        // 减库存
        reduceGoodsStock(items);

        // 将选中的商品从购物车中删除
        removeCarts(carts);

        // 返回VO对象
        OrderVo vo = assembleOrderVo(order, items);
        return new BaseResponse(ResultEnum.SUCCESS.getCode(), vo);
    }

    @Override
    public BaseResponse cancelOrder(Integer userId, Long orderNo) {
        OrderInfo order = orderInfoMapper.getByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return BaseResponse.getErrorMessage("用户不存在此订单");
        }
        if (order.getStatus() >= OrderStatusEnum.PAID.getCode()) {
            return BaseResponse.getErrorMessage("用户已付款，请等待退款功能");
        }

        OrderInfo newOrder = new OrderInfo();
        newOrder.setId(order.getId());
        newOrder.setStatus(OrderStatusEnum.CANCELED.getCode());
        int rowCount = orderInfoMapper.updateByPrimaryKeySelective(newOrder);
        if (rowCount > 0) {
            return BaseResponse.getSuccessMessage("取消订单成功");
        }
        return BaseResponse.getErrorMessage("取消订单失败");
    }

    @Override
    public BaseResponse getOrderGoods(Integer userId) {
        List<UserCart> carts = cartMapper.getCheckedByUserId(userId);
        if (CollectionUtils.isEmpty(carts)) {
            return BaseResponse.getErrorMessage("未选中购物车中的商品");
        }

        BaseResponse baseResponse = assembleOrderItem(userId, carts);
        if (!baseResponse.isSuccess()) {
            return baseResponse;
        }
        List<OrderItem> items = (List<OrderItem>) baseResponse.getData();

        BigDecimal payment = new BigDecimal("0");
        List<OrderItemVo> itemVos = Lists.newArrayList();
        for (OrderItem item : items) {
            itemVos.add(assembleOrderItemVo(item));
            payment = BigDecimalUtil.add(payment.doubleValue(), item.getTotalPrice().doubleValue());
        }

        OrderGoodsVo orderGoodsVo = new OrderGoodsVo();
        orderGoodsVo.setItemVos(itemVos);
        orderGoodsVo.setTotal(payment);
        orderGoodsVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

        return new BaseResponse(ResultEnum.SUCCESS.getCode(), orderGoodsVo);
    }

    @Override
    public BaseResponse detail(Integer userId, Long orderNo) {
        OrderInfo order = orderInfoMapper.getByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            return BaseResponse.getErrorMessage("用户不存在此订单");
        }
        List<OrderItem> items = orderItemMapper.listByUserIdAndOrderNo(userId, orderNo);
        OrderVo orderVo = assembleOrderVo(order, items);

        return new BaseResponse(ResultEnum.SUCCESS.getCode(), orderVo);
    }

    @Override
    public BaseResponse listOrder(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<OrderInfo> orders;
        if (userId == null) {
            orders = orderInfoMapper.listOrder();
        } else {
            orders = orderInfoMapper.listByUserId(userId);
        }

        if (CollectionUtils.isEmpty(orders)) {
            return BaseResponse.getErrorMessage("暂无订单信息");
        }

        List<OrderVo> orderVos = Lists.newArrayList();
        for (OrderInfo order : orders) {
            List<OrderItem> items = orderItemMapper.listByUserIdAndOrderNo(userId, order.getOrderNo());
            orderVos.add(assembleOrderVo(order, items));
        }
        PageInfo pageInfo = new PageInfo(orderVos);

        return new BaseResponse(ResultEnum.SUCCESS.getCode(), pageInfo);
    }

    @Override
    public BaseResponse detailByAdmin(long orderNo) {
        OrderInfo order = orderInfoMapper.getByOrderNo(orderNo);
        if (order == null) {
            return BaseResponse.getErrorMessage("用户不存在此订单");
        }
        List<OrderItem> items = orderItemMapper.listByUserIdAndOrderNo(order.getUserId(), orderNo);
        OrderVo orderVo = assembleOrderVo(order, items);

        return new BaseResponse(ResultEnum.SUCCESS.getCode(), orderVo);
    }

    @Override
    public BaseResponse send(long orderNo) {
        OrderInfo order = orderInfoMapper.getByOrderNo(orderNo);
        if (order == null) {
            return BaseResponse.getErrorMessage("暂无此订单信息");
        }
        // 修改订单状态
        order.setStatus(OrderStatusEnum.SHIPPED.getCode());
        order.setSendTime(new Date());
        //TODO 记录单号
        orderInfoMapper.updateByPrimaryKeySelective(order);

        return new BaseResponse(ResultEnum.SUCCESS.getCode(), "发货成功");
    }

    @Override
    public BaseResponse search(long orderNo, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        OrderInfo order = orderInfoMapper.getByOrderNo(orderNo);
        if (order == null) {
            return BaseResponse.getErrorMessage("暂无此订单信息");
        }
        List<OrderItem> items = orderItemMapper.listByUserIdAndOrderNo(order.getUserId(), orderNo);
        OrderVo orderVo = assembleOrderVo(order, items);

        PageInfo pageInfo = new PageInfo(Lists.newArrayList(orderVo));
        return new BaseResponse(ResultEnum.SUCCESS.getCode(), pageInfo);
    }


    private OrderVo assembleOrderVo(OrderInfo order, List<OrderItem> items) {
        OrderVo vo = new OrderVo();
        vo.setOrderNo(order.getOrderNo());

        vo.setPayment(order.getPayment());
        vo.setPostage(order.getPostage());
        vo.setPaymentType(order.getPaymentType());
        vo.setPaymentTypeDesc(PayPlatformEnum.codeOf(order.getPaymentType()).getValue());

        vo.setStatus(order.getStatus());
        vo.setStatusDesc(OrderStatusEnum.codeOf(order.getStatus()).getDesc());

        vo.setCreateTime(DateTimeUtil.toString(order.getCreateTime()));
        vo.setUpdateTime(DateTimeUtil.toString(order.getUpdateTime()));
        vo.setSendTime(DateTimeUtil.toString(order.getSendTime()));
        vo.setCloseTime(DateTimeUtil.toString(order.getCloseTime()));
        vo.setEndTime(DateTimeUtil.toString(order.getEndTime()));

        UserAddress address = addressMapper.selectByPrimaryKey(order.getAddressId());
        if (address != null) {
            vo.setAddress(assembleAddressVo(address));
        }

        List<OrderItemVo> itemVos = Lists.newArrayList();
        for (OrderItem item : items) {
            itemVos.add(assembleOrderItemVo(item));
        }
        vo.setItems(itemVos);

        vo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

        return vo;
    }

    private OrderItemVo assembleOrderItemVo(OrderItem item) {
        OrderItemVo vo = new OrderItemVo();
        vo.setOrderNo(item.getOrderNo());
        vo.setGoodsId(item.getGoodsId());
        vo.setGoodsName(item.getGoodsName());
        vo.setGoodsImage(item.getGoodsImage());

        vo.setPrice(item.getCurrentUnitPrice());
        vo.setQuantity(item.getQuantity());
        vo.setTotal(item.getTotalPrice());
        vo.setCreateTime(DateTimeUtil.toString(item.getCreateTime()));
        return vo;
    }

    private AddressVo assembleAddressVo(UserAddress address) {
        AddressVo vo = new AddressVo();
        vo.setId(address.getId());
        vo.setName(address.getReceiverName());
        vo.setPhone(address.getReceiverPhone());
        vo.setMobile(address.getReceiverMobile());
        vo.setProvince(address.getReceiverProvince());
        vo.setCity(address.getReceiverCity());
        vo.setDistrict(address.getReceiverDistrict());
        vo.setAddress(address.getReceiverAddress());
        vo.setZip(address.getReceiverZip());

        return vo;
    }

    private void removeCarts(List<UserCart> carts) {
        cartMapper.removeByList(carts);
    }

    private void reduceGoodsStock(List<OrderItem> items) {
        for (OrderItem item : items) {
            GoodsInfo goods = goodsMapper.selectByPrimaryKey(item.getGoodsId());
            goods.setStock(goods.getStock() - item.getQuantity());
            goodsMapper.updateByPrimaryKey(goods);
        }
    }

    private BigDecimal sumCartTotal(List<OrderItem> items) {
        BigDecimal payment = new BigDecimal("0");
        for (OrderItem item : items) {
            payment = BigDecimalUtil.add(payment.doubleValue(), item.getTotalPrice().doubleValue());
        }
        return payment;
    }

    private OrderInfo assembleOrder(Integer userId, Integer addressId, BigDecimal payment) {
        OrderInfo order = new OrderInfo();
        long orderNo = generatorOrderNo();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setAddressId(addressId);

        order.setPayment(payment);
        order.setPostage(0);
        order.setPaymentType(PayPlatformEnum.ALIPAY.getCode());

        order.setStatus(OrderStatusEnum.NON_PAY.getCode());
        int rowCount = orderInfoMapper.insert(order);
        if (rowCount > 0) {
            return order;
        }
        return null;
    }

    private long generatorOrderNo() {
        long orderNo = System.currentTimeMillis();
        return orderNo + new Random().nextInt(100);
    }

    private BaseResponse assembleOrderItem(Integer userId, List<UserCart> carts) {
        List<OrderItem> items = Lists.newArrayList();
        for (UserCart cart : carts) {
            GoodsInfo goods = goodsMapper.selectByPrimaryKey(cart.getGoodsId());
            if (goods.getStatus() != GoodsDict.StatusEnum.ON_SALE.getCode()) {
                return BaseResponse.getErrorMessage("商品：" + goods.getName() + "已下架");
            }
            if (cart.getQuantity() > goods.getStock()) {
                return BaseResponse.getErrorMessage("商品：" + goods.getName() + "库存不足");
            }

            OrderItem item = new OrderItem();
            item.setUserId(userId);
            item.setGoodsId(goods.getId());
            item.setGoodsName(goods.getName());
            item.setGoodsImage(goods.getMainImage());
            item.setCurrentUnitPrice(goods.getPrice());
            item.setQuantity(cart.getQuantity());
            item.setTotalPrice(BigDecimalUtil.mul(goods.getPrice().doubleValue(), cart.getQuantity().doubleValue()));
            items.add(item);
        }

        return new BaseResponse(ResultEnum.SUCCESS.getCode(), items);
    }


}
