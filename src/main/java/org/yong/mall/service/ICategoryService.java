package org.yong.mall.service;

import org.yong.mall.dto.BaseResponse;
import org.yong.mall.pojo.GoodsCategory;

import java.util.List;
import java.util.Set;

/**
 * Created by Yong on 2017/6/12.
 */
public interface ICategoryService {

    BaseResponse<GoodsCategory> getCategoryById(Integer id);

    BaseResponse<List<GoodsCategory>> getCategoryChildByParentId(Integer parentId);

    BaseResponse<Set<GoodsCategory>> getCategoryAllChildById(Integer id);
}
