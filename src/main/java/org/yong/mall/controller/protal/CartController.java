package org.yong.mall.controller.protal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yong.mall.dict.CartDict;
import org.yong.mall.dict.UserDict;
import org.yong.mall.dto.BaseResponse;
import org.yong.mall.enums.ResultEnum;
import org.yong.mall.pojo.UserInfo;
import org.yong.mall.service.ICartService;

import javax.servlet.http.HttpSession;

/**
 * Created by Yong on 2017/6/15.
 */
@Controller
@RequestMapping("/user")
public class CartController {

    @Autowired
    ICartService cartService;

    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse addGoodsToCart(HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(UserDict.CURRENT_USER);
        if (userInfo == null) {
            return BaseResponse.getErrorMessage(ResultEnum.NEED_LOGIN.getDesc());
        }

        return cartService.getCart(userInfo.getId());
    }

    @RequestMapping(value = "/cart", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse addGoodsToCart(HttpSession session, Integer goodsId, Integer count) {
        UserInfo userInfo = (UserInfo) session.getAttribute(UserDict.CURRENT_USER);
        if (userInfo == null) {
            return BaseResponse.getErrorMessage(ResultEnum.NEED_LOGIN.getDesc());
        }

        return cartService.addCartGoods(userInfo.getId(), goodsId, count);
    }

    @RequestMapping(value = "/cart", method = RequestMethod.DELETE)
    @ResponseBody
    public BaseResponse removeCartGoods(HttpSession session, String goodsIds) {
        UserInfo user = (UserInfo) session.getAttribute(UserDict.CURRENT_USER);
        if (user == null) {
            return BaseResponse.getErrorMessage(ResultEnum.NEED_LOGIN.getDesc());
        }
        return cartService.removeCartGoods(user.getId(), goodsIds);
    }

    @RequestMapping(value = "/cart/{goodsId}/check", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse checkCartGoods(@PathVariable("goodsId") Integer goodsId, HttpSession session) {
        UserInfo user = (UserInfo) session.getAttribute(UserDict.CURRENT_USER);
        if (user == null) {
            return BaseResponse.getErrorMessage(ResultEnum.NEED_LOGIN.getDesc());
        }
        return cartService.updateChecked(user.getId(), goodsId, CartDict.Check.CHECK);
    }

    @RequestMapping(value = "/cart/{goodsId}/uncheck", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse uncheckedCartGoods(@PathVariable("goodsId") Integer goodsId, HttpSession session) {
        UserInfo user = (UserInfo) session.getAttribute(UserDict.CURRENT_USER);
        if (user == null) {
            return BaseResponse.getErrorMessage(ResultEnum.NEED_LOGIN.getDesc());
        }
        return cartService.updateChecked(user.getId(), goodsId, CartDict.Check.UN_CHECK);
    }

    @RequestMapping(value = "/cart/check", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse checkCart(HttpSession session) {
        UserInfo user = (UserInfo) session.getAttribute(UserDict.CURRENT_USER);
        if (user == null) {
            return BaseResponse.getErrorMessage(ResultEnum.NEED_LOGIN.getDesc());
        }
        return cartService.updateChecked(user.getId(), null, CartDict.Check.CHECK);
    }

    @RequestMapping(value = "/cart/uncheck", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse uncheckedCart(HttpSession session) {
        UserInfo user = (UserInfo) session.getAttribute(UserDict.CURRENT_USER);
        if (user == null) {
            return BaseResponse.getErrorMessage(ResultEnum.NEED_LOGIN.getDesc());
        }
        return cartService.updateChecked(user.getId(), null, CartDict.Check.UN_CHECK);
    }

    @RequestMapping(value = "/cart/count", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse countCart(HttpSession session) {
        UserInfo user = (UserInfo) session.getAttribute(UserDict.CURRENT_USER);
        if (user == null) {
            return new BaseResponse(ResultEnum.SUCCESS.getCode(), 0);
        }
        return cartService.countCart(user.getId());
    }


}
