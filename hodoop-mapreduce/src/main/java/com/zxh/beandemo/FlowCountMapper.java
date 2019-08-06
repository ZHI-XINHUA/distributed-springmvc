package com.zxh.beandemo;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 流量统计mapper
 */
public class FlowCountMapper extends Mapper<LongWritable, Text,Text,FlowBean> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        //读取行
        //1	13736230513	192.196.100.1	www.atguigu.com	2481	24681	200
        //2	13846544121	192.196.100.2			264	0	200
        String line = value.toString();

        //切割
        String[] words = line.split("\t");

        //手机号码
        String phoneNum = words[1];

        //取出上行流量和下行流量
        long upFlow = Long.parseLong(words[words.length-3]);
        long downFlow = Long.parseLong(words[words.length-2]);

        //写出mapper数据
        context.write(new Text(phoneNum),new FlowBean(upFlow,downFlow));

    }
}
