package com.zxh.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.io.*;
import java.net.URI;

/**
 * fdfs api 操作
 * api官网：http://hadoop.apache.org/docs/current/api/org/apache/hadoop/fs/FileSystem.html
 */
public class FdfsApp {
    public static final String HDFS_URL ="hdfs://192.168.3.46:9000";

    FileSystem fileSystem = null;
    Configuration configuration = null;

    @Before
    public void setUp(){
        configuration = new Configuration();
        //configuration.setBoolean("dfs.support.append", true);
        try {
            //连接hdfs服务器
            fileSystem = FileSystem.get(new URI(HDFS_URL),configuration,"root");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown(){
         //释放资源
        configuration = null;
        fileSystem = null;
    }

    /**
     * 创建文件夹
     * @throws IOException
     */
    @Test
    public void mkdir() throws IOException {
        fileSystem.mkdirs(new Path("/client/apitest"));
    }

    /**
     * 创建文件，并写入内容
     * @throws Exception
     */
    @Test
    public void createFile() throws Exception{
       /* FSDataOutputStream stream = fileSystem.create(new Path("/client/apitest/test1.txt"));
        stream.write("hello hadoop!".getBytes());
        stream.flush();
        stream.close();*/

        FSDataOutputStream stream1 = fileSystem.create(new Path("/client/apitest/test3.txt"), new Progressable() {
            @Override
            public void progress() {
                System.out.print("##");  //带进度提醒信息
            }
        });
        stream1.write("hello hadoop!!!!!!!!!!!!!!".getBytes());
        stream1.flush();
        stream1.close();
    }

    /**
     * 查询内容
     * @throws Exception
     */
    @Test
    public void cat() throws Exception{
        FSDataInputStream inputStream = fileSystem.open(new Path("/client/apitest/test1.txt"));
        IOUtils.copyBytes(inputStream,System.out,1024);
        inputStream.close();
    }

    /**
     * 重命名
     * @throws Exception
     */
    @Test
    public void rename() throws Exception{
        fileSystem.rename(new Path("/client/apitest/test3.txt"),new Path("/client/apitest/test3-1.txt"));
    }

    /**
     * 判断是否存在文件/文件夹
     * @throws Exception
     */
    @Test
    public void exists() throws Exception{
        boolean exists = fileSystem.exists(new Path("/client/apitest/test1.txt"));
        System.out.println("是否存在【/client/apitest/test1.txt】："+exists);
    }

    /**
     * 本地文件上传到hdfs
     * @throws Exception
     */
    @Test
    public void copyFromLocalFile() throws Exception{
        //方式一：copyFromLocalFile上传
        //fileSystem.copyFromLocalFile(new Path("D:\\testfile\\test.doc"),new Path("/client/apitest/test.doc"));

        //方式二：IOUtils 带进度条上传 大文件上传
        //上传的本地文件
        InputStream inputStream = new BufferedInputStream( new FileInputStream(new File("F:\\tool\\linux\\hadoop-2.9.2.tar.gz")));
        //创建文件并显示进度条
        FSDataOutputStream outputStream = fileSystem.create(new Path("/client/soft/hadoop-2.9.2.tar.gz"), new Progressable() {
            @Override
            public void progress() {
                System.out.print("##");
            }
        });
        IOUtils.copyBytes(inputStream,outputStream,4096);

    }

    /**
     * 下载HDFS文件
     * @throws Exception
     */
    @Test
    public void copyToLocalFile() throws Exception{
        fileSystem.copyToLocalFile(new Path("/client/apitest/test.doc"),new Path("D:\\testfile\\test1.doc"));
    }




    /**
     * 删除
     * @throws IOException
     */
    @Test
    public void delete() throws IOException{
        //true：递归删除
        fileSystem.delete(new Path("/client"),true);
    }

    /**
     * 查看某个目录下的所有文件
     * @throws Exception
     */
    @Test
    public void listFiles( ) throws Exception{
        //true 递归查询所有文件，注意：是文件 不包含文件夹
        RemoteIterator<LocatedFileStatus> iterator = fileSystem.listFiles(new Path("/"),true);
        while (iterator.hasNext()){
            LocatedFileStatus fileStatus = iterator.next();
            String dir = fileStatus.isDirectory()?"文件夹：":"文件：";
            int replicationNum = fileStatus.getReplication();//副本数
            long size = fileStatus.getLen();
            String path = fileStatus.getPath().toString();

            System.out.println(dir + "\t block数：" + replicationNum + "\t 大小：" + size + "\t 路径：" + path);
        }
    }

    @Test
    public void listFiles1( ) throws Exception{
        seachFile("/");
    }


    public void seachFile(String pathString) throws Exception{
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path(pathString));
        for (FileStatus fileStatus : fileStatuses){
            String dir = fileStatus.isDirectory()?"文件夹：":"文件：";
            int replicationNum = fileStatus.getReplication();//副本数
            long size = fileStatus.getLen();
            String path = fileStatus.getPath().toString();
            System.out.println(dir + "\t block数：" + replicationNum + "\t 大小：" + size + "\t 路径：" + path);

            if(fileStatus.isDirectory()){
                seachFile(path.replace(HDFS_URL,""));
            }

        }

    }


}
