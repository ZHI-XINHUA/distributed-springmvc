package com.zxh.zk.zkcurator.utils;


import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * 分布式锁
 */
public class DistributedLock {
    private static Logger logger = LoggerFactory.getLogger(DistributedLock.class);

    /**客户端**/
    private CuratorFramework client;

    /**用于挂起当前请求，并等待上一个分布式*/
    private static CountDownLatch zkLockLatch = new CountDownLatch(1);

    /**命名空间**/
    private final String  NAMESPACE ="zk-curator-connector";

    /**分布式锁的总节点名**/
    private static final String  ZK_LOCK_PROJECT = "zxh-locks";

    /**分布式锁节点**/
    private static final String DISCRIBUTED_LOCK = "discributed-lock";

    /**
     * 构造器 传入CuratorFramework客户端
     * @param client
     */
    public DistributedLock(CuratorFramework client){
        this.client = client;
    }

    /**
     * 初始化分布式锁
     */
    public void init(){
        //命名空间
        client = client.usingNamespace(NAMESPACE);
        /*
        * 创建zk分布式锁的父节点：规则务必规范易懂，参考项目工作空间
        *    zk-curator-connector ：命名空间，可以是项目名
        *       |
        *       ——zxh-locks : 模块
        *              |
        *              ——discributed-lock ;分布式锁
        *
        *
        *
         */
        try{
            if(client.checkExists().forPath("/"+ZK_LOCK_PROJECT)==null){
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT) //持久化节点
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE) //完全开放
                        .forPath("/"+ZK_LOCK_PROJECT);
            }
            //对zk分布式节点，添加watcher事件监听
            addWatcherToLock("/"+ZK_LOCK_PROJECT);
            logger.info("分布式锁初始化成功！");
        }catch (Exception e){
            logger.error("分布式锁初始化失败！");
        }
    }

    /**
     * watche节点监听
     * @param path 监听的节点（节点创建、删除、修改 和子节点的修改）
     */
    public void addWatcherToLock(String path) throws Exception {
        final PathChildrenCache cache = new PathChildrenCache(client,path,true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                //子节点删除
                if(event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)){
                    //删除的子节点
                    String path = event.getData().getPath();
                    logger.info("上一个会话已经释放或已断开，节点路径为："+path);
                    if(path.contains(DISCRIBUTED_LOCK)){
                        logger.info("释放计数器，让当前请求获的分布式锁...");
                        zkLockLatch.countDown();
                    }
                }
            }
        });
    }


    /**
     * 获得锁
     */
    public void getLock(){
        //死循环，仅当锁释放且当前请求获得锁成功后才跳出循环
        while(true){
            try {
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath("/"+ZK_LOCK_PROJECT+"/"+DISCRIBUTED_LOCK);
                logger.info("获得分布式锁成功...");
                return;
            } catch (Exception e) {
                //e.printStackTrace();
                logger.info("获得分布式锁失败...");

                try {
                    //如果没有获得分布式锁，则需要重新设置同步资源值
                    if(zkLockLatch.getCount()<=0){
                        logger.info("没有获得分布式锁，则需要重新设置同步资源值!");
                        zkLockLatch = new CountDownLatch(1);
                    }
                    // 阻塞线程
                    zkLockLatch.await();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }



    /**
     * 释放分布式锁
     * @return
     */
    public boolean releaseLock(){
        try{
            String path = "/"+ZK_LOCK_PROJECT+"/"+DISCRIBUTED_LOCK;
            if(client.checkExists().forPath(path)!=null){
                client.delete().forPath(path);
            }
        }catch (Exception e){
            logger.info("分布式锁释放失败，错误信息："+e.getMessage());
            return false;
        }
        logger.info("分布式锁释放完毕！");
        return true;
    }

}
