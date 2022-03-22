package com.evan.distributedzklock.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * 基于【zookeeper】实现分布式锁
 */
@Slf4j
public class ZkLock implements AutoCloseable, Watcher {

    private final ZooKeeper zooKeeper;
    /**
     * 当前正在创建的子节点
     */
    private String znode;

    public ZkLock() throws IOException {
        this.zooKeeper = new ZooKeeper("localhost:2181",
                20000, this);
    }

    public boolean getLock(String businessCode) {
        try {
            // 创建业务 根节点
            Stat stat = zooKeeper.exists("/" + businessCode, false);
            if (stat == null) {
                zooKeeper.create("/" + businessCode, businessCode.getBytes(),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
            }

            // 创建瞬时有序节点  /order/order_00000001
            znode = zooKeeper.create("/" + businessCode + "/" + businessCode + "_", businessCode.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
            log.info("znode:{}", znode);

            // 获取业务节点下 所有的子节点
            List<String> childrenNodes = zooKeeper.getChildren("/" + businessCode, false);
            // 子节点排序
            Collections.sort(childrenNodes);
            // 获取序号最小的（第一个）子节点
            String firstNode = childrenNodes.get(0);
            // 如果创建的节点是第一个子节点，则获得锁
            if (znode.endsWith(firstNode)) {
                return true;
            }
            // 不是第一个子节点，则子节点中第0个为前一个节点
            String lastNode = firstNode;
            for (String node : childrenNodes) {
                if (znode.endsWith(node)) {
                    // 如果是当前正在创建的节点，则给当前节点设置监听前一个节点
                    zooKeeper.exists("/" + businessCode + "/" + lastNode, true);
                    break;
                } else {
                    // 否则，将lastNode的序号增加1，直到lastNode为当前创建节点的前一个节点
                    lastNode = node;
                }
            }
            // 使用线程锁，当对前一个节点设置监听后，线程进入等待状态
            synchronized (this) {
                wait();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void close() throws Exception {
        zooKeeper.delete(znode, -1);
        zooKeeper.close();
        log.info("我已经释放了锁！");
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Event.EventType.NodeDeleted) {
            synchronized (this) {
                notify();
            }
        }
    }
}
