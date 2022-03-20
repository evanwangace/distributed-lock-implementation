package com.evan.distributedlock.controller;

import com.evan.distributedlock.lock.RedisLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
public class RedisController {

    private final RedisTemplate redisTemplate;

    /**
     * 基于【Redis】实现分布式锁
     */
    @RequestMapping("redisLock")
    public String redisLock() {
        log.info("我进入了方法！");
        try (RedisLock redisLock = new RedisLock(redisTemplate, "redisKey", 30)) {
            if (redisLock.getLock()) {
                log.info("我进入了锁！！");
                Thread.sleep(15000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("方法执行完成");
        return "方法执行完成";
    }
}
