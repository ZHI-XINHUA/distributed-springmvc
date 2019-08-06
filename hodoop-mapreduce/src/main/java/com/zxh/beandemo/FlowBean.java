package com.zxh.beandemo;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 流量实体bean  实现序列化Writable
 *  第一步：实现writable接口
 */
public class FlowBean implements Writable {
    /**上传流量**/
    private long upFlow;
    /**下载流量**/
    private long downFlow;
    /**总流量**/
    private long sumFlog;

    //第二步：无参构造器，反序列化时，需要反射调用空参构造函数
    public FlowBean(){
        super();
    }

    public FlowBean(long upFlow, long downFlow) {
        this.upFlow = upFlow;
        this.downFlow = downFlow;
        this.sumFlog = upFlow+downFlow;
    }

    //第三步：写序列化方法
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(upFlow);
        dataOutput.writeLong(downFlow);
        dataOutput.writeLong(sumFlog);
    }

    //第四步：写反序列化方法
    //注意：反序列化方法读顺序必须和写序列化方法的写顺序必须一致
    @Override
    public void readFields(DataInput dataInput) throws IOException {
        upFlow = dataInput.readLong();
        downFlow = dataInput.readLong();
        sumFlog = dataInput.readLong();
    }


    public long getUpFlow() {
        return upFlow;
    }

    public void setUpFlow(long upFlow) {
        this.upFlow = upFlow;
    }

    public long getDownFlow() {
        return downFlow;
    }

    public void setDownFlow(long downFlow) {
        this.downFlow = downFlow;
    }

    public long getSumFlog() {
        return sumFlog;
    }

    public void setSumFlog(long sumFlog) {
        this.sumFlog = sumFlog;
    }

    @Override
    public String toString() {
        return "FlowBean{" +
                "upFlow=" + upFlow +
                ", downFlow=" + downFlow +
                ", sumFlog=" + sumFlog +
                '}';
    }
}
