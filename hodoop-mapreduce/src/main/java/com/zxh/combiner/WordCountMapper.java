package com.zxh.combiner;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


/**
 * 单词拆分Mapper
 *    实现Mapper
 *      LongWritable：行key，偏移量
 *      Text:行 value值
 *      Text: 输出mapper key值
 *      IntWritable：输出mapper   value值
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    //输出的key value
    Text k = new Text();
    IntWritable v = new IntWritable(1);


    /**
     * 分割中的每个键/值对只调用一次这方法
     * @param key  行偏移量
     * @param value  行value
     * @param context  上下文
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //1、读取行
        String line = value.toString();
        //2、切割单词
        String[] words = line.split(" ");
        //3、输出mapper
        for (String word:words) {
            if("".equals(word.trim())) continue;
            k.set(word);
            //System.out.println("map>>>key="+key+" value="+v);
            context.write(k,v);
        }

    }

}
