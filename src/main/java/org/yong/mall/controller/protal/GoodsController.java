package org.yong.mall.controller.protal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.yong.mall.dto.BaseResponse;
import org.yong.mall.service.IGoodsService;

/**
 * Created by Yong on 2017/6/15.
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    IGoodsService goodsService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse detail(@PathVariable("id") Integer id) {
        return goodsService.getGoodsDetailByStatus(id);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse search(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "category") Integer category,
                               @RequestParam(value = "order", defaultValue = " ") String order,
                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return goodsService.listGoodsByNameAndCategoryIds(name, category, order, pageNum, pageSize);
    }
}
