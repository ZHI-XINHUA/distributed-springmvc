package com.zxh.writablecomparable1;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 实现WritableComparable!compareTo方法排序
 */
public class FlowBean implements WritableComparable<FlowBean> {
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

    /**
     * 实行排序
     * @param o
     * @return
     */
    @Override
    public int compareTo(FlowBean o) {
        // 按照总流量大小，倒序排列
        if(sumFlog>o.getSumFlog())
            return -1;

        if(sumFlog<o.getSumFlog())
            return 1;

        return 0;
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
