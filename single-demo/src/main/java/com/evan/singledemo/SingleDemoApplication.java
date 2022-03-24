package com.evan.singledemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.evan.singledemo.dao")
@SpringBootApplication
public class SingleDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SingleDemoApplication.class, args);
    }

}
