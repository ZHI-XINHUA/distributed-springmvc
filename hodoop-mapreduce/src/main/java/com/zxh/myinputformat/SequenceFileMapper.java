package com.zxh.myinputformat;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * mapper  不做任何处理，直接写出
 */
public class SequenceFileMapper extends Mapper<Text, BytesWritable,Text,BytesWritable> {
    @Override
    protected void map(Text key, BytesWritable value, Context context) throws IOException, InterruptedException {
        context.write(key,value);
    }
}
