package org.yong.mall.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
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

    UserCart getCartByUserIdAndGoodsId(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);

    List<UserCart> listCartByUserId(@Param("userId") Integer userId);

    Integer countAllCheckByUserId(@Param("userId") Integer userId);

    int removeCartGoods(@Param("userId") Integer userId, @Param("idList") List<String> idList);

    int updateChecked(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId, @Param("checked") Integer
            checked);

    int countCart(@Param("userId") Integer userId);

    List<UserCart> getCheckedByUserId(@Param("userId") Integer userId);

    int removeByList(@Param("cartList") List<UserCart> cartList);
}