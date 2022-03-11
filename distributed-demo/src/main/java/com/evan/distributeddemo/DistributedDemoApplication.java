package com.evan.distributeddemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.evan.distributeddemo.dao")
public class DistributedDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedDemoApplication.class, args);
    }

}
