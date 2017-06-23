package org.yong.mall.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.yong.mall.pojo.OrderInfo;
import org.yong.mall.pojo.OrderInfoExample;

public interface OrderInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    List<OrderInfo> selectByExample(OrderInfoExample example);

    OrderInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);

    OrderInfo getByUserIdAndOrderNo(@Param("userId") Integer userId,@Param("orderNo") Long orderNo);

    OrderInfo getByOrderNo(@Param("orderNo") Long orderNo);

    List<OrderInfo> listByUserId(@Param("userId") Integer userId);

    List<OrderInfo> listOrder();
}