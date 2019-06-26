package com.zxh.mybatis.service.impl;

import com.zxh.mybatis.entity.Orders;
import com.zxh.mybatis.mapper.OrderMapper;
import com.zxh.mybatis.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderMapper orderMapper;

    @Override
    public Orders buy(String id) {
       return  orderMapper.selectById(id);
    }
}
