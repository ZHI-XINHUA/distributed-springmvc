package com.zxh.partition;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * 自定义分区
 */
public class ProvincePartitioner extends Partitioner<Text,FlowBean> {
    @Override
    public int getPartition(Text key, FlowBean flowBean, int i) {
        int partition = 4;
        // 获取电话号码的前三位
        String prePhoneNum = key.toString().substring(0,3);

        //判断前三位，反正不同的分区
        if ("136".equals(prePhoneNum)) {
            partition = 0;
        }else if ("137".equals(prePhoneNum)) {
            partition = 1;
        }else if ("138".equals(prePhoneNum)) {
            partition = 2;
        }else if ("139".equals(prePhoneNum)) {
            partition = 3;
        }

        return partition;

    }
}
