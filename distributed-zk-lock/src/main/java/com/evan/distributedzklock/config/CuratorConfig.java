package com.evan.distributedzklock.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author evan
 * @date 2022-03-22
 */
@Configuration
public class CuratorConfig {

    @Bean(initMethod = "start", destroyMethod = "close")
    public CuratorFramework getCuratorFramework() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(5000, 3);
        return CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
    }
}