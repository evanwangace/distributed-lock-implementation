package com.evan.distributeddemo.service.impl;

import com.evan.distributeddemo.dao.OrderItemMapper;
import com.evan.distributeddemo.dao.OrderMapper;
import com.evan.distributeddemo.dao.ProductMapper;
import com.evan.distributeddemo.model.Order;
import com.evan.distributeddemo.model.OrderItem;
import com.evan.distributeddemo.model.Product;
import com.evan.distributeddemo.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 通过【查询更新后的商品库存的逻辑】解决超卖问题
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceLogic implements OrderService {

    private final OrderMapper orderMapper;

    private final OrderItemMapper orderItemMapper;

    private final ProductMapper productMapper;

    private int purchaseProductId = 100100;

    private int purchaseProductNum = 1;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer createOrder() throws Exception {
        Product product = productMapper.selectByPrimaryKey(purchaseProductId);
        if (product == null) {
            throw new Exception("购买商品：" + purchaseProductId + "不存在");
        }

        // 展示每个线程获取的商品当前库存
        Integer currentCount = product.getCount();
        log.info("{} 库存数：{}", Thread.currentThread().getName(), currentCount);
        // 校验库存
        if (purchaseProductNum > currentCount) {
            throw new Exception("商品：" + purchaseProductId + "仅剩" + currentCount + "件，无法购买");
        }
        // 更新库存数量
        productMapper.updateProductCount(purchaseProductNum, "xxx", new Date(), product.getId());
        // 检索更新后的商品的库存
        Product newestProduct = productMapper.selectByPrimaryKey(purchaseProductId);
        // 如果商品的库存为负数，抛出异常
        if (newestProduct.getCount() < 0) {
            throw new Exception("商品：" + purchaseProductId + "的库存数为负，购买失败");
        }

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
        return order.getId();
    }
}
