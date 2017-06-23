package org.yong.mall.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.yong.mall.pojo.OrderItem;
import org.yong.mall.pojo.OrderItemExample;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    List<OrderItem> selectByExample(OrderItemExample example);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    List<OrderItem> listByUserIdAndOrderNo(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);

    int batchInsert(@Param("orderItems") List<OrderItem> orderItems);
}