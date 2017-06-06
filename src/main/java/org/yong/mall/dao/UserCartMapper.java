package org.yong.mall.dao;

import java.util.List;
import org.yong.mall.pojo.UserCart;
import org.yong.mall.pojo.UserCartExample;

public interface UserCartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCart record);

    int insertSelective(UserCart record);

    List<UserCart> selectByExample(UserCartExample example);

    UserCart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCart record);

    int updateByPrimaryKey(UserCart record);
}