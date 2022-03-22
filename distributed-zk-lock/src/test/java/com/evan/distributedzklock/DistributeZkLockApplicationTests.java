package com.evan.distributedzklock;

import com.evan.distributedzklock.lock.ZkLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
}
