package org.yong.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yong.mall.dao.GoodsCategoryMapper;
import org.yong.mall.dao.GoodsInfoMapper;
import org.yong.mall.dict.GoodsDict;
import org.yong.mall.dto.BaseResponse;
import org.yong.mall.enums.ResultEnum;
import org.yong.mall.pojo.GoodsCategory;
import org.yong.mall.pojo.GoodsInfo;
import org.yong.mall.service.ICategoryService;
import org.yong.mall.service.IGoodsService;
import org.yong.mall.util.DateTimeUtil;
import org.yong.mall.util.PropertiesUtil;
import org.yong.mall.vo.GoodsDetailVo;
import org.yong.mall.vo.GoodsListVo;

import java.util.List;
import java.util.Set;

/**
 * Created by Yong on 2017/6/12.
 */
@Service
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    GoodsInfoMapper goodsMapper;

    @Autowired
    GoodsCategoryMapper categoryMapper;

    @Autowired
    ICategoryService categoryService;

    @Override
    public BaseResponse saveOrUpdateGoods(GoodsInfo goodsInfo) {
        if (goodsInfo != null) {
            if (goodsInfo.getId() != null) {
                int rowCount = goodsMapper.updateByPrimaryKeySelective(goodsInfo);
                if (rowCount > 0) {
                    return BaseResponse.getSuccessMessage("修改商品成功");
                } else {
                    return BaseResponse.getSuccessMessage("修改商品失败");
                }
            } else {
                int rowCount = goodsMapper.insert(goodsInfo);
                if (rowCount > 0) {
                    return BaseResponse.getSuccessMessage("新增商品成功");
                } else {
                    return BaseResponse.getSuccessMessage("新增商品失败");
                }
            }
        } else {
            return BaseResponse.getErrorMessage("参数错误");
        }
    }

    @Override
    public BaseResponse<String> updateGoodsStatus(Integer goodsId, Integer status) {
        if (goodsId == null || status == null) {
            return BaseResponse.getErrorMessage("参数错误");
        }

        GoodsInfo info = new GoodsInfo();
        info.setId(goodsId);
        info.setStatus(status);
        int rowCount = goodsMapper.updateByPrimaryKeySelective(info);
        if (rowCount > 0) {
            return BaseResponse.getSuccess("修改产品销售状态成功");
        } else {
            return BaseResponse.getErrorMessage("修改产品销售状态失败");
        }
    }

    @Override
    public BaseResponse<GoodsDetailVo> getGoodsDetail(Integer id) {
        if (id == null) {
            return BaseResponse.getErrorMessage("参数错误");
        }

        GoodsInfo info = goodsMapper.selectByPrimaryKey(id);
        if (info == null) {
            return BaseResponse.getErrorMessage("商品不存在或已下架");
        }

        GoodsDetailVo vo = assembleGoodsDetail(info);
        return new BaseResponse<GoodsDetailVo>(ResultEnum.SUCCESS.getCode(), vo);
    }

    @Override
    public BaseResponse<PageInfo> listGoods(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsInfo> goodsInfoList = goodsMapper.listGoods();

        List<GoodsListVo> voList = Lists.newArrayList();
        for (GoodsInfo info : goodsInfoList) {
            GoodsListVo vo = assembleGoodsList(info);
            voList.add(vo);
        }

        PageInfo pageResult = new PageInfo(goodsInfoList);
        pageResult.setList(voList);
        return new BaseResponse<>(ResultEnum.SUCCESS.getCode(), pageResult);
    }

    @Override
    public BaseResponse<PageInfo> listGoodsByNameAndId(String name, Integer id, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(name)) {
            name = new StringBuilder().append("%").append(name).append("%").toString();
        }

        List<GoodsInfo> goodsInfoList = goodsMapper.listGoodsByNameAndId(name, id);

        List<GoodsListVo> voList = Lists.newArrayList();
        for (GoodsInfo info : goodsInfoList) {
            GoodsListVo vo = assembleGoodsList(info);
            voList.add(vo);
        }

        PageInfo pageResult = new PageInfo(goodsInfoList);
        pageResult.setList(voList);
        return new BaseResponse<>(ResultEnum.SUCCESS.getCode(), pageResult);
    }

    @Override
    public BaseResponse<GoodsDetailVo> getGoodsDetailByStatus(Integer id) {
        if (id == null) {
            return BaseResponse.getErrorMessage("参数错误");
        }

        GoodsInfo info = goodsMapper.selectByPrimaryKey(id);
        if (info == null) {
            return BaseResponse.getErrorMessage("商品不存在或已下架");
        }
        if (GoodsDict.StatusEnum.ON_SALE.getCode() != info.getStatus()) {
            return BaseResponse.getErrorMessage("此商品已下架");
        }

        GoodsDetailVo vo = assembleGoodsDetail(info);
        return new BaseResponse<>(ResultEnum.SUCCESS.getCode(), vo);
    }

    @Override
    public BaseResponse listGoodsByNameAndCategoryIds(String name, Integer category, String order, Integer pageNum,
                                                      Integer pageSize) {
        if (StringUtils.isBlank(name) && category == null) {
            return BaseResponse.getErrorMessage("参数错误");
        }

        Set<GoodsCategory> categories;
        List<Integer> categoryIds = Lists.newArrayList();
        if (category != null) {
            GoodsCategory goodsCategory = categoryMapper.selectByPrimaryKey(category);
            // 类型不存在且无关键字
            if (goodsCategory == null && StringUtils.isBlank(name)) {
                PageHelper.startPage(pageNum, pageSize);
                List<GoodsListVo> vos = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(vos);
                return new BaseResponse(ResultEnum.SUCCESS.getCode(), pageInfo);
            }
            categories = categoryService.getCategoryAllChildById(goodsCategory.getId()).getData();
            for (GoodsCategory item : categories){
                categoryIds.add(item.getId());
            }
        }

        if (StringUtils.isNotBlank(name)) {
            name = new StringBuilder().append("%").append(name).append("%").toString();
        }

        PageHelper.startPage(pageNum, pageSize);
        // 排序
        if (StringUtils.isNotBlank(order)) {
            if (GoodsDict.Order.PRICE_ASC_DESC.contains(order)) {
                String[] orderArray = order.split("_");
                PageHelper.orderBy(orderArray[0] + " " + orderArray[1]);
            }
        }

        List<GoodsInfo> goodsInfos = goodsMapper.listGoodsByNameAndCategoryIds(name, categoryIds);
        List<GoodsListVo> vos = Lists.newArrayList();
        for (GoodsInfo info : goodsInfos) {
            vos.add(assembleGoodsList(info));
        }

        PageInfo pageInfo = new PageInfo(goodsInfos);
        pageInfo.setList(vos);

        return new BaseResponse(ResultEnum.SUCCESS.getCode(), pageInfo);
    }

    /**
     * 转换 GoodsDetailVo 对象
     *
     * @param goods 源对象
     * @return
     */
    private GoodsDetailVo assembleGoodsDetail(GoodsInfo goods) {
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setId(goods.getId());
        vo.setCategoryId(goods.getCategoryId());
        vo.setName(goods.getName());
        vo.setTitle(goods.getTitle());
        vo.setMainImage(goods.getMainImage());
        vo.setSubImages(goods.getSubImages());
        vo.setPrice(goods.getPrice());
        vo.setStock(goods.getStock());
        vo.setStatus(goods.getStatus());

        vo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.jiayong.me/"));

        GoodsCategory category = categoryMapper.selectByPrimaryKey(goods.getCategoryId());
        if (category == null) {
            // 默认为根节点
            vo.setParentCategoryId(0);
        } else {
            vo.setParentCategoryId(category.getId());
        }

        vo.setCreateTime(DateTimeUtil.toString(goods.getCreateTime()));
        vo.setUpdateTime(DateTimeUtil.toString(goods.getUpdateTime()));

        return vo;
    }

    /**
     * 转换 GoodsListVo 对象
     *
     * @param goods 源对象
     * @return
     */
    private GoodsListVo assembleGoodsList(GoodsInfo goods) {
        GoodsListVo vo = new GoodsListVo();
        vo.setId(goods.getId());
        vo.setCategoryId(goods.getCategoryId());
        vo.setName(goods.getName());
        vo.setTitle(goods.getTitle());
        vo.setMainImage(goods.getMainImage());
        vo.setPrice(goods.getPrice());
        vo.setStatus(goods.getStatus());
        vo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.jiayong.me/"));

        return vo;
    }

}
