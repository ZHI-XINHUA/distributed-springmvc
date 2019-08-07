package com.zxh.partition;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 技术流量reducer
 */
public class FlowCountReducer extends Reducer<Text,FlowBean, Text,FlowBean> {

    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
        //技术流量
        long upFlowSum = 0;
        long downFlowSum = 0;
        for (FlowBean bean: values) {
            upFlowSum += bean.getUpFlow();
            downFlowSum += bean.getDownFlow();
        }

        //写出
        context.write(key,new FlowBean(upFlowSum,downFlowSum));
    }
}
