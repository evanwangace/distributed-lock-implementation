package com.evan.distributedlock.controller;

import com.evan.distributedlock.dao.DistributedLockMapper;
import com.evan.distributedlock.model.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 通过【复制Application.class启动多个】演示普通的锁在多服务跨jvm下的局限性
 */
@RequiredArgsConstructor
@Slf4j
@RestController
public class DemoController {

    private final DistributedLockMapper distributedLockMapper;

    private Lock lock = new ReentrantLock();

    /**
     * 通过【复制DistributedLockApplication启动多个】演示普通的锁在多服务跨jvm下的局限性
     */
    @RequestMapping("singleLock")
    public String singleLock() {
        log.info("我进入了方法！");
        lock.lock();
        try {
            log.info("我进入了锁！");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return "我已经执行完成！";
    }

    /**
     * 基于【数据库】实现分布式锁
     */
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping("distributedLockByDatabase")
    public String distributedLock() throws Exception {
        log.info("我进入了方法！");
        DistributedLock demo = distributedLockMapper.selectDistributedLock("demo");
        if (demo == null) {
            throw new Exception("分布式锁找不到");
        }
        log.info("我进入了锁！");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "我已经执行完成！";
    }
}
