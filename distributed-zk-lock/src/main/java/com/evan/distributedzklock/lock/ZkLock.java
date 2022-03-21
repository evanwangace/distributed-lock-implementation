package com.evan.distributedzklock.lock;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * 基于【zookeeper】实现分布式锁
 */
public class ZkLock implements AutoCloseable, Watcher {

    private ZooKeeper zookeeper;

    @Override
    public void close() {

    }

    @Override
    public void process(WatchedEvent watchedEvent) {

    }
}
