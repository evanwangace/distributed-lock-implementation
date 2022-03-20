package com.evan.distributedlock.controller;

import com.evan.distributedlock.dao.DistributedLockMapper;
import com.evan.distributedlock.model.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
public class DatabaseController {

    private final DistributedLockMapper distributedLockMapper;

    /**
     * 基于【数据库】实现分布式锁
     */
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping("databaseLock")
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
