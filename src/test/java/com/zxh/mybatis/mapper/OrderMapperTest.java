package com.zxh.mybatis.mapper;

import com.zxh.mybatis.BaseSource;
import com.zxh.mybatis.entity.Orders;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * OrderMapper 测试
 */
public class OrderMapperTest extends BaseSource {
    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void selectByidTest(){
        Orders orders = orderMapper.selectById("1");
        System.out.println(orders.getOrderNum());
    }

    @Test
    public void updateTest(){
        Orders orders = new Orders();
        orders.setId("1");
        orders.setOrderNum(11);
        orderMapper.update(orders);
    }

    @Test
    public void insetTest(){
        Orders orders = new Orders();
        orders.setId("2");
        orders.setOrderNum(5);
        orderMapper.insert(orders);
    }
}
