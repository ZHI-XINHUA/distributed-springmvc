package com.zxh.groupingcomparator;

import org.apache.hadoop.io.WritableComparator;

/**
 * tGroupingComparator 分组排序
 */
public class OrderSortGroupingComparator extends WritableComparator {
    //创建一个构造将比较对象的类传给父类
    protected OrderSortGroupingComparator(){
        super(OrderBean.class,true);
    }

    /**
     * 重新compare 排序
     * @param a
     * @param b
     * @return
     */
    @Override
    public int compare(Object a, Object b) {
        OrderBean aBean = (OrderBean) a;
        OrderBean bBean = (OrderBean) b;

        int result;
        if (aBean.getOrderId() > bBean.getOrderId()) {
            result = 1;
        } else if (aBean.getOrderId() < bBean.getOrderId()) {
            result = -1;
        } else {
            result = 0;
        }

        return result;
    }
}
