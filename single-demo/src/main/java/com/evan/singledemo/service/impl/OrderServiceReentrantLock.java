package com.evan.singledemo.service.impl;

import com.evan.singledemo.dao.OrderItemMapper;
import com.evan.singledemo.dao.OrderMapper;
import com.evan.singledemo.dao.ProductMapper;
import com.evan.singledemo.model.Order;
import com.evan.singledemo.model.OrderItem;
import com.evan.singledemo.model.Product;
import com.evan.singledemo.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 通过【使用ReentrantLock加锁】解决超卖问题
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceReentrantLock implements OrderService {

    private final OrderMapper orderMapper;

    private final OrderItemMapper orderItemMapper;

    private final ProductMapper productMapper;

    private final PlatformTransactionManager platformTransactionManager;

    private final TransactionDefinition transactionDefinition;

    private int purchaseProductId = 100100;

    private int purchaseProductNum = 1;

    private Lock lock = new ReentrantLock();

    /**
     * 手动控制事务，解决：第一个线程更新后，事务未提交前，又被第二个线程重新获取数量，第一个和第二个线程获得数量都是1的问题
     * 采用两个事务，解决：在同一把锁内控制事务，并且避免事务的嵌套
     *
     * @return
     * @throws Exception
     */
    @Override
    public Integer createOrder() throws Exception {
        Product product;

        // 手动加锁
        lock.lock();
        try {
            TransactionStatus transactionStatus1 = platformTransactionManager.getTransaction(transactionDefinition);
            product = productMapper.selectByPrimaryKey(purchaseProductId);
            if (product == null) {
                // 手动回滚事务
                platformTransactionManager.rollback(transactionStatus1);
                throw new Exception("购买商品：" + purchaseProductId + "不存在");
            }

            // 展示每个线程获取的商品当前库存
            Integer currentCount = product.getCount();
            log.info("{} 库存数：{}", Thread.currentThread().getName(), currentCount);
            // 校验库存
            if (purchaseProductNum > currentCount) {
                // 手动回滚事务
                platformTransactionManager.rollback(transactionStatus1);
                throw new Exception("商品：" + purchaseProductId + "仅剩" + currentCount + "件，无法购买");
            }
            // 更新库存数量
            productMapper.updateProductCount(purchaseProductNum, "xxx", new Date(), product.getId());
            platformTransactionManager.commit(transactionStatus1);
        } finally {
            lock.unlock();
        }

        // 手动获取事务
        TransactionStatus transactionStatus2 = platformTransactionManager.getTransaction(transactionDefinition);
        Order order = new Order();
        order.setOrderAmount(product.getPrice().multiply(new BigDecimal(purchaseProductNum)));
        // 待处理
        order.setOrderStatus(1);
        order.setReceiverName("xxx");
        order.setReceiverMobile("13311112222");
        order.setCreateTime(new Date());
        order.setCreateUser("xxx");
        order.setUpdateTime(new Date());
        order.setUpdateUser("xxx");
        orderMapper.insertSelective(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(order.getId());
        orderItem.setProductId(product.getId());
        orderItem.setPurchasePrice(product.getPrice());
        orderItem.setPurchaseNum(purchaseProductNum);
        orderItem.setCreateUser("xxx");
        orderItem.setCreateTime(new Date());
        orderItem.setUpdateTime(new Date());
        orderItem.setUpdateUser("xxx");
        orderItemMapper.insertSelective(orderItem);

        // 手动提交事务
        platformTransactionManager.commit(transactionStatus2);

        return order.getId();
    }
}
