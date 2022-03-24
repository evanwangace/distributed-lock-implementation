package com.evan.singledemo.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Product {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 商品描述
     */
    private String productDesc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateUser;
}