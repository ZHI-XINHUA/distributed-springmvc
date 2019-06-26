package com.zxh.mybatis.controller;

import com.zxh.mybatis.entity.Orders;
import com.zxh.mybatis.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/buy",method = RequestMethod.GET)
    public String buy(){
        Orders orders = orderService.buy("1");
        System.out.println(orders.getOrderNum());
        return "buy";
    }
}
