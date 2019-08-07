package com.zxh.partition;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class FlowCountDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1、 获取配置信息以及job
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        //2、设置job加载路径(反射使用)
        job.setJarByClass(FlowCountDriver.class);

        //3、设置map和reduce类
        job.setMapperClass(FlowCountMapper.class);
        job.setReducerClass(FlowCountReducer.class);

        //4、设置map输出key value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);

        //5、设置最终输出kv类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        //6、设置输入和输出路径
        FileInputFormat.setInputPaths(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));

        //指定自定义数据分区
        job.setPartitionerClass(ProvincePartitioner.class);

        //同时指定相应数量的reduce task
        job.setNumReduceTasks(5);

        //7、提交任务
        boolean result = job.waitForCompletion(true);

        System.exit(result ? 0 : 1);
    }
}
