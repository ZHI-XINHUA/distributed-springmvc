package com.zxh.keyvaluetextinputformatdemo;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;

public class KVTextMapper extends Mapper<Text,Text,Text, LongWritable> {
    Logger logger = Logger.getLogger(KVTextMapper.class);

    LongWritable v = new LongWritable(1);

    @Override
    protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
        logger.info("KVTextMapper==> key="+key+" value="+value);
        context.write(key,v);
    }
}
