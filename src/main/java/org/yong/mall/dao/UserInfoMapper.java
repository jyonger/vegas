package org.yong.mall.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.yong.mall.pojo.UserInfo;
import org.yong.mall.pojo.UserInfoExample;

public interface UserInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    List<UserInfo> selectByExample(UserInfoExample example);

    UserInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    int checkUsername(@Param("username") String username);

    UserInfo getUserInfo(@Param("username") String username, @Param("password") String password);

    int checkEmail(@Param("email") String email);

    String getUserQuestion(@Param("username") String username);

    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);
}