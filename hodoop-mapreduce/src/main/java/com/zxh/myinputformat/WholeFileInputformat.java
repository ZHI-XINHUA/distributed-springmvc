package com.zxh.myinputformat;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

/**
 * 自定义的Inputformat 必须继承FileInputFormat，重写createRecordReader
 */
public class WholeFileInputformat extends FileInputFormat<Text, BytesWritable> {

    /**
     * 重写 isSplitable,返回false 不可切割
     * @param context
     * @param filename
     * @return
     */
    @Override
    protected boolean isSplitable(JobContext context, Path filename) {
        return false;
    }

    /**
     * 重写createRecordReader，创建自定义RecordReader，实现一次读取一个完整的文件封装为K/V
     * @param inputSplit
     * @param taskAttemptContext
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public RecordReader<Text, BytesWritable> createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        WholeRecordReader wholeRecordReader = new WholeRecordReader();
        wholeRecordReader.initialize(inputSplit,taskAttemptContext);
        return wholeRecordReader;
    }
}
