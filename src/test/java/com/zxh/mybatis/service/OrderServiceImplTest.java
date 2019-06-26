package com.zxh.mybatis.service;

import com.sun.scenario.effect.impl.prism.PrImage;
import com.zxh.mybatis.BaseSource;
import com.zxh.mybatis.entity.Orders;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderServiceImplTest extends BaseSource {
    @Autowired
    private OrderService orderService;

    @Test
    public void buyTest(){
        Orders orders = orderService.buy("1");
        System.out.println(orders.getOrderNum());
    }
}
