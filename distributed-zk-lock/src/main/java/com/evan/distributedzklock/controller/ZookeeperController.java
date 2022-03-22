package com.evan.distributedzklock.controller;

import com.evan.distributedzklock.lock.ZkLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ZookeeperController {

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
}
