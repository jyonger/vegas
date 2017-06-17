package org.yong.mall.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.yong.mall.pojo.UserAddress;
import org.yong.mall.pojo.UserAddressExample;

public interface UserAddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAddress record);

    int insertSelective(UserAddress record);

    List<UserAddress> selectByExample(UserAddressExample example);

    UserAddress selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserAddress record);

    int updateByPrimaryKey(UserAddress record);

    int updateByIdAndUserId(UserAddress address);

    int removeByIdAndUser(@Param("userId") Integer userId, @Param("addressId") Integer addressId);

    List<UserAddress> listAddressByUser(Integer userId);
}