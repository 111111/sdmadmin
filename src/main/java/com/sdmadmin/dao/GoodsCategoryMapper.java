package com.sdmadmin.dao;

import com.sdmadmin.entity.GoodsCategory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsCategory record);

    int insertSelective(GoodsCategory record);

    GoodsCategory selectByPrimaryKey(Integer id);

    GoodsCategory selectByParameter(GoodsCategory goodsCategory);

    int updateByPrimaryKeySelective(GoodsCategory record);

    int updateByPrimaryKey(GoodsCategory record);
}