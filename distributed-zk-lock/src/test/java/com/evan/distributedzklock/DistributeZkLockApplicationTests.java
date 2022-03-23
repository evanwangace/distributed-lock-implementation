package com.evan.distributedzklock;

import com.evan.distributedzklock.lock.ZkLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributeZkLockApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void testZkLock() throws Exception {
        ZkLock zkLock = new ZkLock();
        boolean lock = zkLock.getLock("order");
        log.info("获得锁的结果：" + lock);
        zkLock.close();
    }

    @Test
    public void testCuratorLock() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        client.start();
        InterProcessMutex lock = new InterProcessMutex(client, "/order");
        try {
            if (lock.acquire(30, TimeUnit.SECONDS)) {
                try {
                    log.info("我获得了锁！！！");
                } finally {
                    lock.release();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.close();
    }
}
