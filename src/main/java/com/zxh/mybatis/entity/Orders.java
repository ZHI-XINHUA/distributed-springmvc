package com.zxh.mybatis.entity;

import java.io.Serializable;

public class Orders implements Serializable {

    private static final long serialVersionUID = -2997951818177102897L;

    private String id;
    private int orderNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }
}
