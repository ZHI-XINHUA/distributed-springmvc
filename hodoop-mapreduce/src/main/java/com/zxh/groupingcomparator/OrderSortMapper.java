package com.zxh.groupingcomparator;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * mapper读取行，输出key为OrderBean（因为key才能排序），value是NullWritable（空值）
 */
public class OrderSortMapper extends Mapper<LongWritable, Text,OrderBean, NullWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 1 获取一行
        String line = value.toString();

        // 2 截取
        String[] fields = line.split("\t");
        int orderId = Integer.parseInt(fields[0]);
        double price = Double.parseDouble(fields[2]);

        //3、写出
        context.write(new OrderBean(orderId,price),NullWritable.get());
    }
}
