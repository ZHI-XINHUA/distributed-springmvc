package com.zxh.keyvaluetextinputformatdemo;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

import java.io.IOException;

public class KVTextReduce extends Reducer<Text, LongWritable,Text,LongWritable> {
    Logger logger = Logger.getLogger(KVTextReduce.class);
    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        logger.info("KVTextReduce==>>key="+key+" values="+values.toString());
        long sum =0;
        for (LongWritable value:values) {
            sum += value.get();
        }
        context.write(key,new LongWritable(sum));
    }
}
