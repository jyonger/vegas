package org.yong.mall.service;

import org.yong.mall.dto.BaseResponse;
import org.yong.mall.pojo.UserInfo;

/**
 * Created by Yong on 2017/6/6.
 */
public interface IUserService {

    BaseResponse<UserInfo> login(String username, String password);

    BaseResponse<String> register(UserInfo userInfo);

    BaseResponse<String> checkUnique(String value, String type);

    BaseResponse<String> getUserQuestion(String username);

    BaseResponse<String> checkUserQuestionAndAnswer(String username,String question,String answer);

    boolean isAdmin(UserInfo userInfo);
}
