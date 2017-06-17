package org.yong.mall.service;

import com.github.pagehelper.PageInfo;
import org.yong.mall.dto.BaseResponse;
import org.yong.mall.pojo.GoodsInfo;
import org.yong.mall.vo.GoodsDetailVo;

/**
 * Created by Yong on 2017/6/12.
 */
public interface IGoodsService {

    BaseResponse saveOrUpdateGoods(GoodsInfo goodsInfo);

    BaseResponse<String> updateGoodsStatus(Integer goodsId, Integer status);

    BaseResponse<GoodsDetailVo> getGoodsDetail(Integer id);

    BaseResponse<PageInfo> listGoods(Integer pageNum, Integer pageSize);

    BaseResponse<PageInfo> listGoodsByNameAndId(String name, Integer id, Integer pageNum, Integer pageSize);

    BaseResponse<GoodsDetailVo> getGoodsDetailByStatus(Integer id);

    BaseResponse listGoodsByNameAndCategoryIds(String name, Integer category, String order, Integer pageNum, Integer pageSize);
}
