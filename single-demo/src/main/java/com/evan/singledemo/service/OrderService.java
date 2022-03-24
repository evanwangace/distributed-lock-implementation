package com.evan.singledemo.service;

/**
 * 订单Service
 *
 * @author evan
 * @date 2022-03-16
 */
public interface OrderService {
    /**
     * 创建订单
     *
     * @return 订单id
     * @throws Exception
     */
    Integer createOrder() throws Exception;
}
