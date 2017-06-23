package org.yong.mall.controller.protal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.yong.mall.dict.UserDict;
import org.yong.mall.dto.BaseResponse;
import org.yong.mall.enums.ResultEnum;
import org.yong.mall.pojo.UserInfo;
import org.yong.mall.service.IOrderService;

import javax.servlet.http.HttpSession;

/**
 * Created by Yong on 2017/6/19.
 */
@Controller
@RequestMapping("/user/order")
public class OrderController {

    @Autowired
    IOrderService orderService;

    @RequestMapping(value = "/{orderNo}/pay/status", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse payStatus(HttpSession session, @PathVariable("orderNo") Long orderNo) {
        UserInfo user = (UserInfo) session.getAttribute(UserDict.CURRENT_USER);
        if (user == null) {
            return new BaseResponse(ResultEnum.NEED_LOGIN.getCode(), ResultEnum.NEED_LOGIN.getDesc());
        }

        BaseResponse baseResponse = orderService.getPayStatusByUserIdAndOrderNo(user.getId(), orderNo);
        if (baseResponse.isSuccess()) {
            return new BaseResponse(ResultEnum.SUCCESS.getCode(), true);
        }
        return new BaseResponse(ResultEnum.ERROR.getCode(), false);
    }

    /**
     * 结算购物车
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse cartCheckout(HttpSession session, Integer addressId) {
        UserInfo user = (UserInfo) session.getAttribute(UserDict.CURRENT_USER);
        if (user == null) {
            return new BaseResponse(ResultEnum.NEED_LOGIN.getCode(), ResultEnum.NEED_LOGIN.getDesc());
        }

        return orderService.cartCheckout(user.getId(), addressId);
    }

    /**
     * 获取购物车中已经选中的商品信息
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/cart/checked", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse getOrderGoods(HttpSession session) {
        UserInfo user = (UserInfo) session.getAttribute(UserDict.CURRENT_USER);
        if (user == null) {
            return new BaseResponse(ResultEnum.NEED_LOGIN.getCode(), ResultEnum.NEED_LOGIN.getDesc());
        }

        return orderService.getOrderGoods(user.getId());
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse listOrder(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int
            pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        UserInfo user = (UserInfo) session.getAttribute(UserDict.CURRENT_USER);
        if (user == null) {
            return new BaseResponse(ResultEnum.NEED_LOGIN.getCode(), ResultEnum.NEED_LOGIN.getDesc());
        }

        return orderService.listOrder(user.getId(), pageNum, pageSize);
    }

    @RequestMapping(value = "/{orderNo}/detail", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse detail(HttpSession session, @PathVariable("orderNo") Long orderNo) {
        UserInfo user = (UserInfo) session.getAttribute(UserDict.CURRENT_USER);
        if (user == null) {
            return new BaseResponse(ResultEnum.NEED_LOGIN.getCode(), ResultEnum.NEED_LOGIN.getDesc());
        }

        return orderService.detail(user.getId(), orderNo);
    }


    @RequestMapping(value = "/{orderNo}/cancel", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse cancelOrder(HttpSession session, @PathVariable("orderNo") Long orderNo) {
        UserInfo user = (UserInfo) session.getAttribute(UserDict.CURRENT_USER);
        if (user == null) {
            return new BaseResponse(ResultEnum.NEED_LOGIN.getCode(), ResultEnum.NEED_LOGIN.getDesc());
        }

        return orderService.cancelOrder(user.getId(), orderNo);
    }


}
