package com.zxh.mybatis.mapper;
import com.zxh.mybatis.entity.Orders;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

//@Repository
public interface OrderMapper {

    Orders selectById(@Param("id") String id);

    void update(Orders orders);

    void insert(Orders orders);
}
