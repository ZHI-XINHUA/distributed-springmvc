package com.zxh.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 单词计数Reducer
 *   Reducer
 *     Text：mapper输出的key
 *     IntWritable：mapper输出的value
 *     Text：reducer输出结果的key
 *     IntWritable：reducer输出结果的value
 */
public class WordCountReducer extends Reducer<Text, IntWritable,Text,IntWritable> {
    int sum ;
    IntWritable v = new IntWritable();
    /**
     * 计算个数
     * @param key
     * @param values
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        //1、累计求和
        sum = 0;
        for (IntWritable count :values) {
            sum += count.get();
        }
        v.set(sum);
        //2、输出计算结果
        context.write(key,v);
    }
}
