package org.yong.mall.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.yong.mall.dto.BaseResponse;
import org.yong.mall.service.IOrderService;

import javax.servlet.http.HttpSession;

/**
 * Created by Yong on 2017/6/21.
 */
@Controller("adminOrderController")
@RequestMapping("/admin/order")
public class OrderController {

    @Autowired
    IOrderService orderService;

    @RequestMapping(value = "/{orderNo}/detail", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse detail(HttpSession session, @PathVariable("orderNo") long orderNo) {
        return orderService.detailByAdmin(orderNo);
    }

    @RequestMapping(value = "/{orderNo}/send", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse send(HttpSession session, @PathVariable("orderNo") long orderNo) {
        return orderService.send(orderNo);
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public BaseResponse listOrder(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int
            pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return orderService.listOrder(null, pageNum, pageSize);
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse search(HttpSession session, long orderNo, @RequestParam(value = "pageNum", defaultValue =
            "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        return orderService.search(orderNo, pageNum, pageSize);
    }


}
