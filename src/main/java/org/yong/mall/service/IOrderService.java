package org.yong.mall.service;

import org.yong.mall.dto.BaseResponse;

/**
 * Created by Yong on 2017/6/19.
 */
public interface IOrderService {
    BaseResponse getPayStatusByUserIdAndOrderNo(Integer userId, Long orderNo);

    BaseResponse cartCheckout(Integer userId, Integer addressId);

    BaseResponse cancelOrder(Integer userId, Long orderNo);

    BaseResponse getOrderGoods(Integer userId);

    BaseResponse detail(Integer userId, Long orderNo);

    BaseResponse listOrder(Integer userId, int pageNum, int pageSize);

    BaseResponse detailByAdmin(long orderNo);

    BaseResponse send(long orderNo);

    /**
     * 搜索订单
     * 待修改 ： 根据订单信息查询
     *
     * @param no
     * @param pageNum
     * @param pageSize
     */
    BaseResponse search(long no, int pageNum, int pageSize);
}
