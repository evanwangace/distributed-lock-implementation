package com.evan.distributedlock.dao;

import com.evan.distributedlock.model.DistributedLock;
import com.evan.distributedlock.model.DistributedLockExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface DistributedLockMapper {
    long countByExample(DistributedLockExample example);

    int deleteByExample(DistributedLockExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DistributedLock record);

    int insertSelective(DistributedLock record);

    List<DistributedLock> selectByExample(DistributedLockExample example);

    DistributedLock selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DistributedLock record, @Param("example") DistributedLockExample example);

    int updateByExample(@Param("record") DistributedLock record, @Param("example") DistributedLockExample example);

    int updateByPrimaryKeySelective(DistributedLock record);

    int updateByPrimaryKey(DistributedLock record);

    DistributedLock selectDistributedLock(@Param("businessCode") String businessCode);
}