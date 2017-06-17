package org.yong.mall.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.yong.mall.pojo.GoodsCategory;
import org.yong.mall.pojo.GoodsInfo;
import org.yong.mall.pojo.GoodsInfoExample;

public interface GoodsInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsInfo record);

    int insertSelective(GoodsInfo record);

    List<GoodsInfo> selectByExample(GoodsInfoExample example);

    GoodsInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsInfo record);

    int updateByPrimaryKey(GoodsInfo record);

    List<GoodsInfo> listGoods();

    List<GoodsInfo> listGoodsByNameAndId(@Param("name") String name, @Param("id") Integer id);

    List<GoodsInfo> listGoodsByNameAndCategoryIds(@Param("name") String name, @Param("categories") List<Integer> categories);
}