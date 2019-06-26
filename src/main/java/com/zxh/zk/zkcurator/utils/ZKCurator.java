package com.zxh.zk.zkcurator.utils;

import org.apache.curator.framework.CuratorFramework;

/**
 * 自定义zk client
 */
public class ZKCurator {
    /**命名空间**/
    private final String  namespace ="zk-curator-connector";

    private CuratorFramework client = null;

    public ZKCurator(CuratorFramework client ){
        this.client = client;
    }

    /**
     * 初始化
     */
    public void init(){
        //使用命名空间
        client = client.usingNamespace(namespace);
    }

    /**
     * 判断zk是否连接
     * @return
     */
    public boolean isZKAlive(){
        return client.isStarted();
    }
}
