package com.zxh.groupingcomparator;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class OrderBean implements WritableComparable<OrderBean> {
    //订单(数字字符串)
    private int orderId;
    //价格
    private double price;

    public OrderBean() {
        super();
    }


    public OrderBean(int orderId, double price) {
        this.orderId = orderId;
        this.price = price;
    }



    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(orderId);
        dataOutput.writeDouble(price);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        orderId = dataInput.readInt();
        price = dataInput.readDouble();
    }

    // 二次排序
    @Override
    public int compareTo(OrderBean o) {
        if(orderId>o.getOrderId()) {
            return 1;
        }else if(orderId<o.getOrderId()){
            return -1;
        }else{
            // 价格倒序排序
            return price>o.getPrice()? -1:1;
        }
    }

    public String toString(){
        return orderId+" "+price;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
