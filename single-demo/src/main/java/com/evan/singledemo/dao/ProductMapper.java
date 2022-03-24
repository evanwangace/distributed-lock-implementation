package com.evan.singledemo.dao;

import com.evan.singledemo.model.Product;
import com.evan.singledemo.model.ProductExample;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ProductMapper {
    long countByExample(ProductExample example);

    int deleteByExample(ProductExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    List<Product> selectByExample(ProductExample example);

    Product selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Product record, @Param("example") ProductExample example);

    int updateByExample(@Param("record") Product record, @Param("example") ProductExample example);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    int updateProductCount(@Param("purchaseProductNum") int purchaseProductNum,
                           @Param("updateUser") String xxx, @Param("updateTime") Date date,
                           @Param("id") Integer id);
}