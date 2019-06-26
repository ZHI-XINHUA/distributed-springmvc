package com.zxh.mybatis.entity;

import java.io.Serializable;

/**
 * 商品
 */
public class Merchandise implements Serializable {
    //主键
    private String id;
    //数量
    private int num;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
