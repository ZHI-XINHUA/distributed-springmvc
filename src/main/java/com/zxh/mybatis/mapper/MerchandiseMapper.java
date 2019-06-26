package com.zxh.mybatis.mapper;
import com.zxh.mybatis.entity.Merchandise;
import org.apache.ibatis.annotations.Param;

public interface MerchandiseMapper {

    Merchandise selectById(@Param("id") String id);

    void update(Merchandise merchandise);
}
