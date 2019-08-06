package com.zxh.nlineinputformatdemo;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class NLineReduce extends Reducer<Text, LongWritable,Text,LongWritable> {
    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {

        //汇总
        int sum =0;
        for (LongWritable value : values){
            sum += value.get();
        }

        //输出
        context.write(new Text(key),new LongWritable(sum));
    }
}
