package org.yong.mall.controller.protal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yong.mall.dict.UserDict;
import org.yong.mall.dto.BaseResponse;
import org.yong.mall.enums.ResultEnum;
import org.yong.mall.pojo.UserAddress;
import org.yong.mall.pojo.UserInfo;
import org.yong.mall.service.IAddressService;

import javax.servlet.http.HttpSession;

/**
 * Created by Yong on 2017/6/16.
 */
@Controller
@RequestMapping("/user/address")
public class AddressController {

    @Autowired
    IAddressService addressService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse add(HttpSession session, UserAddress address) {
        UserInfo userInfo = (UserInfo) session.getAttribute(UserDict.CURRENT_USER);
        if (userInfo == null) {
            return new BaseResponse(ResultEnum.NEED_LOGIN.getCode(), ResultEnum.NEED_LOGIN.getDesc());
        }

        return addressService.add(userInfo.getId(), address);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse update(HttpSession session, UserAddress address) {
        UserInfo user = (UserInfo) session.getAttribute(UserDict.CURRENT_USER);
        if (user == null) {
            return new BaseResponse(ResultEnum.NEED_LOGIN.getCode(), ResultEnum.NEED_LOGIN.getDesc());
        }

        return addressService.update(user.getId(), address);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse update(HttpSession session, Integer id) {
        UserInfo user = (UserInfo) session.getAttribute(UserDict.CURRENT_USER);
        if (user == null) {
            return new BaseResponse(ResultEnum.NEED_LOGIN.getCode(), ResultEnum.NEED_LOGIN.getDesc());
        }

        return addressService.remove(user.getId(), id);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse list(HttpSession session) {
        UserInfo user = (UserInfo) session.getAttribute(UserDict.CURRENT_USER);
        if (user == null) {
            return new BaseResponse(ResultEnum.NEED_LOGIN.getCode(), ResultEnum.NEED_LOGIN.getDesc());
        }

        return addressService.listAddressByUser(user.getId());
    }

}
