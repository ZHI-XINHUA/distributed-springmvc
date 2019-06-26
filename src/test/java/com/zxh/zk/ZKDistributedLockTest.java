package com.zxh.zk;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.zxh.mybatis.entity.Merchandise;
import com.zxh.mybatis.entity.Orders;
import com.zxh.mybatis.mapper.MerchandiseMapper;
import com.zxh.mybatis.mapper.OrderMapper;
import com.zxh.zk.zkcurator.utils.DistributedLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * zookeeper分布式锁测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml",
    "classpath:zk/spring-zookeeper.xml"})
public class ZKDistributedLockTest {
    final static Logger log = LoggerFactory.getLogger(ZKDistributedLockTest.class);
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private MerchandiseMapper merchandiseMapper;

    @Autowired
    private DistributedLock distributedLock;

    /**
     * 并发测试分布式锁
     */
    @Test
    public void bugTest(){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            System.out.println("创建线程" + i);

            executorService.submit(()->{
                bug();
            });
        }

        executorService.shutdown();

        //注意 主线程一定保持运行，否则会话会关闭，zk获取锁报错
        try {
            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 测试分布式锁
     */
    @Test
    public void bug(){
        System.out.println("=======but()=======");
        String id = "1";
        try{
            //获取分布式锁
            distributedLock.getLock();

            //获取数据库库存
            Merchandise merchandise = merchandiseMapper.selectById(id);
            if(merchandise.getNum()<=0){
                log.info("库存不足，不能购买");
                //释放锁
                distributedLock.releaseLock();
                return;
            }

            // 2.  模拟一个耗时操作
            boolean isOrderCreated = true;
            try {
                //创建订单
                //参数1为终端ID
                //参数2为数据中心ID
                Snowflake snowflake = IdUtil.createSnowflake(1, 1);
                String orderId = snowflake.nextId()+"";
                Orders orders = new Orders();
                orders.setId(orderId);
                orderMapper.insert(orders);
                TimeUnit.SECONDS.sleep(2);
                isOrderCreated = true;
            } catch (InterruptedException e) {
                isOrderCreated = false;

            }

            if(isOrderCreated){
                log.info("创建订单成功");
                //3、减少库存
                merchandise.setNum(merchandise.getNum()-1);
                merchandiseMapper.update(merchandise);
            }else{
                log.info("创建订单失败");
            }

            //释放锁
            distributedLock.releaseLock();

        }catch (Exception e){
            //释放锁
            distributedLock.releaseLock();
        }



    }
}
