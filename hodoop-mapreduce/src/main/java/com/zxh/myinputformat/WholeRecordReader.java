package com.zxh.myinputformat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * 自定义RecordReader
 */
public class WholeRecordReader extends RecordReader<Text, BytesWritable> {
    /**配置**/
    Configuration conf;
    /**文件分割**/
    private FileSplit fileSplit;
    /**key**/
    private Text key = new Text();
    /**value**/
    private BytesWritable value = new BytesWritable();
    /**正在读取标示**/
    private boolean isProgress = true;

    /**
     * 初始化
     * @param inputSplit
     * @param taskAttemptContext
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        fileSplit = (FileSplit) inputSplit;
        conf = taskAttemptContext.getConfiguration();
    }

    /**
     * 业务处理方法，得到key和value
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if(isProgress) {

            //定义缓冲区
            byte[] buf = new byte[(int)fileSplit.getLength()];

            //获取文件系统
            Path path = fileSplit.getPath();
            FileSystem fileSystem = path.getFileSystem(conf);

            //读取数据
            FSDataInputStream inputStream = fileSystem.open(path);

            //读取数据内容到缓冲区
            IOUtils.readFully(inputStream,buf,0,buf.length);

            //输出文件内容到value
            value.set(buf,0,buf.length);

            //获取文件路径及名称并设置key
            String name  = path.getName().toString();
            key.set(name);

            //标志读取完成
            isProgress = false;

            return true;
        }


        return false;
    }

    /**
     * 获取key
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public Text getCurrentKey() throws IOException, InterruptedException {
        return key;
    }

    /**
     * 获取value
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
        return value;
    }

    /**
     * 进度
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public float getProgress() throws IOException, InterruptedException {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }
}
