package org.yong.mall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.yong.mall.dao.GoodsInfoMapper;
import org.yong.mall.dao.UserCartMapper;
import org.yong.mall.dict.CartDict;
import org.yong.mall.dto.BaseResponse;
import org.yong.mall.enums.ResultEnum;
import org.yong.mall.pojo.GoodsInfo;
import org.yong.mall.pojo.UserCart;
import org.yong.mall.service.ICartService;
import org.yong.mall.util.BigDecimalUtil;
import org.yong.mall.util.PropertiesUtil;
import org.yong.mall.vo.CartGoodsVo;
import org.yong.mall.vo.CartVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Yong on 2017/6/15.
 */
@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    UserCartMapper cartMapper;

    @Autowired
    GoodsInfoMapper goodsMapper;

    @Override
    public BaseResponse<CartVo> addCartGoods(Integer userId, Integer goodsId, Integer count) {
        if (goodsId == null || count == null) {
            return BaseResponse.getErrorMessage("参数不完整");
        }

        UserCart cart = cartMapper.getCartByUserIdAndGoodsId(userId, goodsId);
        if (cart != null) {
            count += cart.getQuantity();
            cart.setQuantity(count);
            if (cartMapper.updateByPrimaryKeySelective(cart) < 0) {
                return BaseResponse.getErrorMessage("加入购物车失败");
            }
        } else {
            cart = new UserCart();
            cart.setUserId(userId);
            cart.setGoodsId(goodsId);
            cart.setQuantity(count);
            if (cartMapper.insertSelective(cart) < 0) {
                return BaseResponse.getErrorMessage("加入购物车失败");
            }
        }

        CartVo vo = getCartVo(userId);
        return new BaseResponse<>(ResultEnum.SUCCESS.getCode(), vo);
    }

    @Override
    public BaseResponse<CartVo> removeCartGoods(Integer userId, String goodsIds) {
        if (goodsIds == null || userId == null) {
            return BaseResponse.getErrorMessage(ResultEnum.ILLEGAL_ARGUMENT.getDesc());
        }

        List<String> idList = Splitter.on(",").splitToList(goodsIds);
        cartMapper.removeCartGoods(userId, idList);

        CartVo vo = getCartVo(userId);
        return new BaseResponse<>(ResultEnum.SUCCESS.getCode(), vo);
    }

    @Override
    public BaseResponse updateChecked(Integer userId, Integer goodsId, Integer checked) {
        if (userId == null) {
            return BaseResponse.getErrorMessage(ResultEnum.ILLEGAL_ARGUMENT.getDesc());
        }

        cartMapper.updateChecked(userId, goodsId, checked);

        CartVo vo = getCartVo(userId);
        return new BaseResponse<>(ResultEnum.SUCCESS.getCode(), vo);
    }

    @Override
    public BaseResponse countCart(Integer userId) {
        int count = cartMapper.countCart(userId);
        return new BaseResponse(ResultEnum.SUCCESS.getCode(), count);
    }

    @Override
    public BaseResponse getCart(Integer userId) {
        CartVo vo = getCartVo(userId);
        return new BaseResponse<>(ResultEnum.SUCCESS.getCode(), vo);
    }

    private CartVo getCartVo(Integer userId) {
        CartVo vo = new CartVo();
        List<CartGoodsVo> cartGoodsVoList = Lists.newArrayList();
        BigDecimal cartTotal = new BigDecimal("0");
        boolean allCheck = true;

        List<UserCart> carts = cartMapper.listCartByUserId(userId);
        if (!CollectionUtils.isEmpty(carts)) {
            for (UserCart cart : carts) {
                CartGoodsVo goodsVo = new CartGoodsVo();
                goodsVo.setId(cart.getId());

                // 商品信息
                GoodsInfo goodsInfo = goodsMapper.selectByPrimaryKey(cart.getGoodsId());
                if (goodsInfo != null) {
                    goodsVo.setGoodsId(goodsInfo.getId());
                    goodsVo.setName(goodsInfo.getName());
                    goodsVo.setTitle(goodsInfo.getTitle());
                    goodsVo.setMainImage(goodsInfo.getMainImage());
                    goodsVo.setPrice(goodsInfo.getPrice());
                    goodsVo.setStock(goodsInfo.getStock());
                    goodsVo.setStatus(goodsInfo.getStatus());

                    if (goodsInfo.getStock() > cart.getQuantity()) {
                        goodsVo.setQuantity(cart.getQuantity());
                        goodsVo.setUpdateStock(false);
                    } else {
                        goodsVo.setQuantity(goodsInfo.getStock());
                        goodsVo.setUpdateStock(true);
                        // 更新数据库中购物车的数据
                        UserCart newCart = new UserCart();
                        newCart.setQuantity(goodsInfo.getStock());
                        newCart.setId(cart.getId());
                        cartMapper.updateByPrimaryKeySelective(newCart);
                    }
                    goodsVo.setTotal(BigDecimalUtil.mul(goodsInfo.getPrice().doubleValue(), goodsVo.getQuantity()
                            .doubleValue()));
                }
                goodsVo.setCheck(cart.getChecked() == CartDict.Check.UN_CHECK ? false : true);

                if (goodsVo.isCheck()) {
                    // 计入总价
                    cartTotal = BigDecimalUtil.add(cartTotal.doubleValue(), goodsVo.getTotal().doubleValue());
                } else {
                    allCheck = false;
                }
                cartGoodsVoList.add(goodsVo);
            }
        }

        vo.setGoodsVoList(cartGoodsVoList);
        vo.setTotal(cartTotal);
        vo.setCheckAll(allCheck);
        vo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return vo;
    }

}
