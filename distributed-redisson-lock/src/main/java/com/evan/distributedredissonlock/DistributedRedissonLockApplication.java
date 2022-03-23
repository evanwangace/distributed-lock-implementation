package com.evan.distributedredissonlock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author evan
 * @date 2022-03-23
 */
@SpringBootApplication
public class DistributedRedissonLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedRedissonLockApplication.class, args);
    }

}
