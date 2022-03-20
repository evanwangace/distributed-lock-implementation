package com.evan.distributedlock.service.impl;

import com.evan.distributedlock.lock.RedisLock;
import com.evan.distributedlock.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 定时任务实现类
 *
 * @author evan
 * @date 2022-03-20
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class SchedulerServiceImpl implements SchedulerService {

    private final RedisTemplate redisTemplate;

    /**
     * 模拟发送短信
     */
    @Scheduled(cron = "0/5 * * * * ?")
    @Override
    public void sendSms() {
        try (RedisLock redisLock = new RedisLock(redisTemplate, "autoSms", 30)) {
            if (redisLock.getLock()) {
                log.info("向18812341234发送短信！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
