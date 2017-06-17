package org.yong.mall.controller.protal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yong.mall.dict.UserDict;
import org.yong.mall.dto.BaseResponse;
import org.yong.mall.enums.ResultEnum;
import org.yong.mall.pojo.UserInfo;
import org.yong.mall.service.IUserService;

import javax.servlet.http.HttpSession;

/**
 * Created by Yong on 2017/6/6.
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    IUserService userService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<UserInfo> login(HttpSession session, String username, String password) {
        BaseResponse<UserInfo> result = userService.login(username, password);
        if (result.isSuccess()) {
            session.setAttribute(UserDict.CURRENT_USER, result.getData());
        }
        return result;
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse<String> logout(HttpSession session) {
        if (session.getAttribute(UserDict.CURRENT_USER) == null) {
            return BaseResponse.getErrorMessage("当前无用户登录");
        }
        session.removeAttribute(UserDict.CURRENT_USER);
        return BaseResponse.getSuccessMessage("当前用户退出成功");
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<String> register(UserInfo userInfo) {
        return userService.register(userInfo);
    }

    @RequestMapping(value = "check", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<String> checkUnique(String value, String type) {
        return userService.checkUnique(value, type);
    }

    @RequestMapping(value = "info", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse<UserInfo> getUserInfo(HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(UserDict.CURRENT_USER);
        if (userInfo == null) {
            return BaseResponse.getErrorMessage("当前无用户登录");
        }

        return new BaseResponse<>(ResultEnum.SUCCESS.getCode(), userInfo);
    }

    @RequestMapping(value = "forget", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse<String> getUserQuestion(String username) {
        return userService.getUserQuestion(username);
    }

    @RequestMapping(value = "check/question", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<String> resetPassword(String username, String question, String answer) {
        return userService.checkUserQuestionAndAnswer(username, question, answer);
    }


}
