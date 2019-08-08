package com.zxh.combiner;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Combiner ：对每一个MapTask的输出进行局部汇总，以减小网络传输量
 */
public class WordcountCombiner extends Reducer<Text, IntWritable,Text,IntWritable> {
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
        int sum = 0;
        for (IntWritable count :values) {
            sum += count.get();
        }
        //System.out.println("key="+key+"  vlaue="+values);
        //2、输出计算结果
        context.write(key,new IntWritable(sum));
    }
}
