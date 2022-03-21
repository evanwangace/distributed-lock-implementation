package com.evan.distributedzklock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DistributedZkLockApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedZkLockApplication.class, args);
    }

}
