package org.yong.mall.service.impl;

import com.sun.xml.internal.rngom.parse.host.Base;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yong.mall.common.RedisCache;
import org.yong.mall.dao.UserInfoMapper;
import org.yong.mall.dict.UserDict;
import org.yong.mall.dto.BaseResponse;
import org.yong.mall.enums.ResultEnum;
import org.yong.mall.pojo.UserInfo;
import org.yong.mall.service.IUserService;
import org.yong.mall.util.MD5Util;

import java.util.UUID;

/**
 * Created by Yong on 2017/6/6.
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    RedisCache redis;

    @Override
    public BaseResponse<UserInfo> login(String username, String password) {
        int checkResult = userInfoMapper.checkUsername(username);
        if (checkResult == 0) {
            return BaseResponse.getErrorMessage("用户名不存在");
        }

        UserInfo info = userInfoMapper.getUserInfo(username, MD5Util.encode(password));
        if (info == null) {
            return BaseResponse.getErrorMessage("账户与密码不匹配");
        }

        return new BaseResponse<>(ResultEnum.SUCCESS.getCode(), info);
    }

    @Override
    public BaseResponse<String> register(UserInfo userInfo) {
        BaseResponse<String> validRepsonse = checkUnique(userInfo.getUsername(), UserDict.USERNAME);
        if (!validRepsonse.isSuccess()) {
            return validRepsonse;
        }
        validRepsonse = checkUnique(userInfo.getEmail(), UserDict.EMAIL);
        if (!validRepsonse.isSuccess()) {
            return validRepsonse;
        }

        userInfo.setPassword(MD5Util.encode(userInfo.getPassword()));
        userInfo.setRole(UserDict.Role.CUSTOMER);

        if (userInfoMapper.insertSelective(userInfo) == 0) {
            return BaseResponse.getErrorMessage("注册失败");
        }
        return BaseResponse.getSuccessMessage("注册成功");
    }

    @Override
    public BaseResponse<String> checkUnique(String value, String type) {
        if (org.apache.commons.lang.StringUtils.isNotBlank(value)) {
            if (UserDict.USERNAME.equals(type)) {
                if (userInfoMapper.checkUsername(value) > 0) {
                    return BaseResponse.getErrorMessage("用户名已存在");
                }
            } else if (UserDict.EMAIL.equals(type)) {
                if (userInfoMapper.checkEmail(value) > 0) {
                    return BaseResponse.getErrorMessage("邮箱已注册");
                }
            }

            return BaseResponse.getSuccessMessage("可以使用");
        } else {
            return BaseResponse.getErrorMessage("参数错误");
        }
    }

    @Override
    public BaseResponse<String> getUserQuestion(String username) {
        BaseResponse validResponse = checkUnique(username, UserDict.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        String question = userInfoMapper.getUserQuestion(username);
        if (StringUtils.isNotBlank(question)) {
            return BaseResponse.getErrorMessage("用户未设置");
        }

        return BaseResponse.getSuccess(question);
    }

    @Override
    public BaseResponse<String> checkUserQuestionAndAnswer(String username, String question, String answer) {
        BaseResponse validResponse = checkUnique(username, UserDict.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        if (userInfoMapper.checkAnswer(username, question, answer) > 0) {
            String token = UUID.randomUUID().toString();
            redis.putCacheWithExpireTime(RedisCache.TOKEN_PREFIX + username, token, RedisCache.CACHETIME * 30);
            return BaseResponse.getSuccess(token);
        }
        return BaseResponse.getErrorMessage("验证失败");
    }

    @Override
    public boolean isAdmin(UserInfo userInfo) {
        return userInfo.getRole() == 0 ? true : false;
    }


}
