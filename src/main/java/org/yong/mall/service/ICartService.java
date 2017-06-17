package org.yong.mall.service;

import org.yong.mall.dto.BaseResponse;
import org.yong.mall.vo.CartVo;

/**
 * Created by Yong on 2017/6/15.
 */
public interface ICartService {

    BaseResponse<CartVo> addCartGoods(Integer userId, Integer goodsId, Integer count);

    BaseResponse<CartVo> removeCartGoods(Integer userId, String goodsIds);

    BaseResponse updateChecked(Integer userId, Integer goodsId,Integer checked);

    BaseResponse countCart(Integer userId);

    BaseResponse getCart(Integer userId);
}
