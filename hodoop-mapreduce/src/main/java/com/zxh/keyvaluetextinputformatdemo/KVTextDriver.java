package com.zxh.keyvaluetextinputformatdemo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueLineRecordReader;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class KVTextDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        // 设置切割符
        conf.set(KeyValueLineRecordReader.KEY_VALUE_SEPERATOR," ");
        //设置配置信息和获取Job实例
        Job job = Job.getInstance(conf);

        //设置job加载路径(反射使用)
        job.setJarByClass(KVTextDriver.class);

        //关联mapper和reducer
        job.setMapperClass(KVTextMapper.class);
        job.setReducerClass(KVTextReduce.class);

        //设置Mapper的key和value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        //设置最终输出的key和value类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //设置输入输出路径
        FileInputFormat.setInputPaths(job,new Path(args[0]));
        // 设置输入格式
        job.setInputFormatClass(KeyValueTextInputFormat.class);

        FileOutputFormat.setOutputPath(job,new Path(args[1]));

        //提交任务
        job.waitForCompletion(true);
    }
}
