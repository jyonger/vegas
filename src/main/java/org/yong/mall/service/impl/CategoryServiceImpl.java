package org.yong.mall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.yong.mall.dao.GoodsCategoryMapper;
import org.yong.mall.dto.BaseResponse;
import org.yong.mall.enums.ResultEnum;
import org.yong.mall.pojo.GoodsCategory;
import org.yong.mall.service.ICategoryService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Yong on 2017/6/12.
 */
@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    GoodsCategoryMapper categoryMapper;

    @Override
    public BaseResponse<GoodsCategory> getCategoryById(Integer id) {
        GoodsCategory category = categoryMapper.selectByPrimaryKey(id);
        if (category == null) {
            return BaseResponse.getErrorMessage("此类型不存在");
        }

        return new BaseResponse<>(ResultEnum.SUCCESS.getCode(), category);
    }

    @Override
    public BaseResponse<List<GoodsCategory>> getCategoryChildByParentId(Integer parentId) {
        List<GoodsCategory> categories = categoryMapper.selectChildCategory(parentId);
        if (CollectionUtils.isEmpty(categories)) {
            return BaseResponse.getErrorMessage("未找到当前分类的子分类");
        }
        return new BaseResponse<>(ResultEnum.SUCCESS.getCode(), categories);
    }

    @Override
    public BaseResponse<Set<GoodsCategory>> getCategoryAllChildById(Integer id) {
        Set<GoodsCategory> categories = new HashSet<>();
        categories = findChildCategory(categories, id);
        if (CollectionUtils.isEmpty(categories)) {
            return BaseResponse.getErrorMessage("未找到当前分类的子分类");
        }

        return new BaseResponse<>(ResultEnum.SUCCESS.getCode(), categories);
    }

    /**
     * 递归获取所有子节点
     *
     * @param categories
     * @param id
     * @return
     */
    private Set<GoodsCategory> findChildCategory(Set<GoodsCategory> categories, Integer id) {
        GoodsCategory category = categoryMapper.selectByPrimaryKey(id);
        if (categories != null) {
            categories.add(category);
        }

        List<GoodsCategory> childes = categoryMapper.selectChildCategory(id);
        for (GoodsCategory item : childes) {
            findChildCategory(categories, item.getId());
        }
        return categories;
    }

}
