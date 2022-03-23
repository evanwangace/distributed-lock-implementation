package com.evan.distributedredissonlock;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DistributedRedissonLockApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void testRedissonLock() {
        Config config = new Config();
        // 根据redis架构不同选择单体或集群模式
        SingleServerConfig serverConfig = config.useSingleServer();
        serverConfig.setAddress("redis://localhost:6379");
        serverConfig.setPassword("root");
        RedissonClient redisson = Redisson.create(config);
        // 通过redission获取锁
        RLock rLock = redisson.getLock("order");
        try {
            rLock.lock(30, TimeUnit.SECONDS);
            log.info("我获得了锁！！！");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log.info("我释放了锁！！");
            rLock.unlock();
        }
    }
}
