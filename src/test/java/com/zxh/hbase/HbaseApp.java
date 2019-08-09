package com.zxh.hbase;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * hbase 操作
 */
public class HbaseApp {
    Connection connection;
    Admin admin;

    @Before
    public void setup(){
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("hbase.zookeeper.quorum", "192.168.3.50");
        configuration.set("hbase.master", "192.168.3.50:60000");
        try {
            connection = ConnectionFactory.createConnection(configuration);
            admin = connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @After
    public void tearDown(){
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //判断表是否存在
    //@Test
    public boolean isTableExist( String tableName) throws IOException {
        //String tableName = "student";
        boolean existFlag = admin.tableExists(TableName.valueOf(tableName));
        System.out.println(tableName +"是否存在："+existFlag);
        return existFlag;
    }

    //创建表
    @Test
    public void createTable() throws IOException {
        //表已存在
        if(isTableExist("student")){
            return;
        }
        TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(TableName.valueOf("student")) //teacher表名
                .setColumnFamily(ColumnFamilyDescriptorBuilder.of("info"))//info 列族
                .setColumnFamily(ColumnFamilyDescriptorBuilder.of("detail")).build(); //detail 列族
        admin.createTable(tableDescriptor);
    }

    //添加列族
    @Test
    public void alterTable() throws IOException {
        TableName tableName = TableName.valueOf("student");
        admin.disableTable(tableName);
        //添加列族
        admin.addColumnFamily(tableName,ColumnFamilyDescriptorBuilder.of("info1"));

         /*
          TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(tableName) //teacher表名
                .setColumnFamily(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("test")).build()).build(); //info 列族
        admin.modifyTable(tableDescriptor); // modifyTable会清掉数据，慎用
        */
        admin.enableTable(tableName);
    }

    //删除表
    @Test
    public void deleteTable() throws IOException {
        //表不存在
        if(!isTableExist("student")){
            return;
        }
        TableName tableName = TableName.valueOf("student");
        //删除之前先禁用表
        admin.disableTable(tableName);
        //删除表
        admin.deleteTable(tableName);
    }

    /**
     * 插入数据
     */
    @Test
    public void addRowData() throws IOException {
        String tableName = "student";//表名
        String rowKey="1001";// 主键
        String family = "info"; //列族
        //获取HTable
        HTable table = (HTable) connection.getTable(TableName.valueOf(tableName));
        //设置rowKey 主键
        Put put = new Put(Bytes.toBytes(rowKey));
        //设置行，参数一：列族，参数二：列名，参数三：值
        put.addColumn(Bytes.toBytes(family),Bytes.toBytes("age"),Bytes.toBytes("29"));
        table.put(put);//已存在则修改

        List<Put> list = new ArrayList<Put>();
        for (int i=0;i<10;i++){
            //设置rowKey 主键
            Put put1 = new Put(Bytes.toBytes(rowKey));
            //设置行，参数一：列族，参数二：列名，参数三：值
            put1.addColumn(Bytes.toBytes(family),Bytes.toBytes("colum"+i),Bytes.toBytes(i+""));
            list.add(put1);
        }
        //批量参入
        table.put(list);

        table.close();

    }

    //删除数据
    @Test
    public void deleteRow() throws IOException {
        String tableName = "student";//表名
        String rowKey="1001";// 主键
        String family = "info"; //列族
        HTable table = (HTable) connection.getTable(TableName.valueOf(tableName));

        //删除student 1001 info:colum1 的值
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        delete.addColumn(Bytes.toBytes(family),Bytes.toBytes("colum1"));
        table.delete(delete);

        //删除student 1001 info
        Delete delete1 = new Delete(Bytes.toBytes(rowKey));
        delete1.addFamily(Bytes.toBytes(family));
        table.delete(delete1);

        //删除student 1001
        Delete delete2 = new Delete(Bytes.toBytes(rowKey));
        table.delete(delete2);

        table.close();
    }

    //获取所有数据
    @Test
    public void getAllRows() throws IOException {
        TableName tableName = TableName.valueOf("student");
        //获取HTable
        HTable hTable = (HTable)connection.getTable(tableName);
        //得到用于扫描region的对象
        Scan scan = new Scan();
        ResultScanner scanner = hTable.getScanner(scan);
        Iterator<Result>  iterator = scanner.iterator();
        while (iterator.hasNext()){
            Result result = iterator.next();
            Cell[] cells = result.rawCells();
            for (Cell cell:cells){
                System.out.print(" 行主键："+ Bytes.toString(CellUtil.cloneRow(cell)));
                System.out.print(" 列族："+ Bytes.toString(CellUtil.cloneFamily(cell)));
                System.out.print(" 列："+Bytes.toString(CellUtil.cloneQualifier(cell)));
                System.out.print(" 值："+Bytes.toString(CellUtil.cloneValue(cell)));
                System.out.println();
            }
        }

    }

    //获取某一行数据
    @Test
    public void getRow() throws IOException {
        TableName tableName = TableName.valueOf("student");
        String rowKey ="1002";
        String family = "info"; //列族
        //获取HTable
        HTable hTable = (HTable)connection.getTable(tableName);
        //设置获取的过滤信息
        Get get = new Get(Bytes.toBytes(rowKey));
        //get.readAllVersions();
        Result result = hTable.get(get);
        Cell[] cells = result.rawCells();
        for (Cell cell:cells){
            System.out.print(" 行主键："+ Bytes.toString(CellUtil.cloneRow(cell)));
            System.out.print(" 列族："+ Bytes.toString(CellUtil.cloneFamily(cell)));
            System.out.print(" 列："+Bytes.toString(CellUtil.cloneQualifier(cell)));
            System.out.print(" 值："+Bytes.toString(CellUtil.cloneValue(cell)));
            System.out.println();
        }

        System.out.println("==================指定“列族:列”的数据==============");
        get.addFamily(Bytes.toBytes(family));
        result = hTable.get(get);
        cells = result.rawCells();
        for (Cell cell:cells){
            System.out.print(" 行主键："+ Bytes.toString(CellUtil.cloneRow(cell)));
            System.out.print(" 列族："+ Bytes.toString(CellUtil.cloneFamily(cell)));
            System.out.print(" 列："+Bytes.toString(CellUtil.cloneQualifier(cell)));
            System.out.print(" 值："+Bytes.toString(CellUtil.cloneValue(cell)));
            System.out.println();
        }

        hTable.close();
    }






}
