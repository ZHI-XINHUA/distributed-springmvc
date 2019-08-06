package com.zxh.nlineinputformatdemo;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * mapper
 */
public class NLineMapper extends Mapper<LongWritable, Text,Text,LongWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //获取行
        String line = value.toString();

        //切割
        String[] words = line.split(" ");
        for (String word:words){
            //写出
            context.write(new Text(word),new LongWritable(1));
        }
    }
}
