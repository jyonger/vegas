package org.yong.mall.controller.protal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yong.mall.dto.BaseResponse;
import org.yong.mall.pojo.GoodsCategory;
import org.yong.mall.service.ICategoryService;

import java.util.List;
import java.util.Set;

/**
 * Created by Yong on 2017/6/12.
 */
@Controller
@RequestMapping("/goods/category/")
public class GoodsCategoryController {
    @Autowired
    ICategoryService categoryService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse<GoodsCategory> getCateGoryById(@PathVariable("id") Integer id) {
        return categoryService.getCategoryById(id);
    }

    @RequestMapping(value = "{id}/child")
    @ResponseBody
    public BaseResponse<List<GoodsCategory>> getCategoryChild(@PathVariable("id")Integer id){
        return categoryService.getCategoryChildByParentId(id);
    }

    @RequestMapping(value = "{id}/child/all")
    @ResponseBody
    public BaseResponse<Set<GoodsCategory>> getCategoryAllChild(@PathVariable("id")Integer id){
        return categoryService.getCategoryAllChildById(id);
    }
}
