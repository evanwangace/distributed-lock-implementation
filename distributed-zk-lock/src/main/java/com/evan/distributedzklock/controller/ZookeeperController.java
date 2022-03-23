package com.evan.distributedzklock.controller;

import com.evan.distributedzklock.lock.ZkLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
@RestController
public class ZookeeperController {

    private final CuratorFramework client;

    /**
     * 基于【zookeeper瞬时节点】实现锁
     */
    @RequestMapping("zkLock")
    public String zookeeperLock() {
        log.info("我进入了方法！");
        try (ZkLock zkLock = new ZkLock()) {
            if (zkLock.getLock("order")) {
                log.info("我获得了锁");
                Thread.sleep(10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("方法执行完成！");
        return "方法执行完成！";
    }

    /**
     * 基于【curator客户端】实现锁
     * 具体参考：https://curator.apache.org/getting-started.html
     */
    @RequestMapping("curatorLock")
    public String curatorLock() {
        log.info("我进入了方法！");
        InterProcessMutex lock = new InterProcessMutex(client, "/order");
        try {
            // 30s为获取锁最大等待时间，超过时间则获取失败
            if (lock.acquire(30, TimeUnit.SECONDS)) {
                log.info("我获得了锁！！");
                Thread.sleep(10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                log.info("我释放了锁！！");
                lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("方法执行完成！");
        return "方法执行完成！";
    }
}
