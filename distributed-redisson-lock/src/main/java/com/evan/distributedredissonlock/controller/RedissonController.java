package com.evan.distributedredissonlock.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author evan
 * @date 2022-03-23
 */
@RequiredArgsConstructor
@Slf4j
@RestController
public class RedissonController {

    @RequestMapping("redissonLock")
    public String redissonLock() {
        // 创建连接
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer();
        serverConfig.setAddress("redis://localhost:6379");
        serverConfig.setPassword("root");
        RedissonClient redisson = Redisson.create(config);
        // 获取锁
        RLock rLock = redisson.getLock("order");
        log.info("我进入了方法！！");
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
        log.info("方法执行完成！！");
        return "方法执行完成！！";
    }
}
