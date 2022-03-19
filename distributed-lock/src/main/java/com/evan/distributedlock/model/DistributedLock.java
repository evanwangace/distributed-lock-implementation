package com.evan.distributedlock.model;

import lombok.Data;

@Data
public class DistributedLock {
    /**
    * 主键id
    */
    private Integer id;

    /**
    * 业务代码
    */
    private String businessCode;

    /**
    * 业务名称
    */
    private String businessName;
}