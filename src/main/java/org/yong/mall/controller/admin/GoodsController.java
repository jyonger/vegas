package org.yong.mall.controller.admin;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.yong.mall.dto.BaseResponse;
import org.yong.mall.pojo.GoodsInfo;
import org.yong.mall.service.IGoodsService;
import org.yong.mall.vo.GoodsDetailVo;

/**
 * Created by Yong on 2017/6/12.
 */
@Controller("adminGoodsController")
@RequestMapping("/admin/goods/")
public class GoodsController {

    @Autowired
    IGoodsService goodsService;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse saveOrUpdateGoods(GoodsInfo goods) {
        return goodsService.saveOrUpdateGoods(goods);
    }

    @RequestMapping(value = "{id}/sale", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<String> goodsSale(@PathVariable("id") Integer id, Integer status) {
        return goodsService.updateGoodsStatus(id, status);
    }

    @RequestMapping(value = "{id}/detail", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse<GoodsDetailVo> goodsSale(@PathVariable("id") Integer id) {
        return goodsService.getGoodsDetail(id);
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse<PageInfo> listGoods(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return goodsService.listGoods(pageNum, pageSize);
    }

    @RequestMapping(value = "search", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<PageInfo> searchGoods(String name, Integer id, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return goodsService.listGoodsByNameAndId(name, id, pageNum, pageSize);
    }

}
