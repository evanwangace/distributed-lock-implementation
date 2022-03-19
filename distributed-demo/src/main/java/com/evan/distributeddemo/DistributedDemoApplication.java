package com.evan.distributeddemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.evan.distributeddemo.dao")
@SpringBootApplication
public class DistributedDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedDemoApplication.class, args);
    }

}
