package com.sdmadmin.dao;

import com.sdmadmin.entity.Goods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Goods record);

    int insertSelective(Goods record);

    List<Goods> selectList(Goods record);

    Goods selectByPrimaryKey(Long id);

    Goods selectByGoodId(String goodId);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);
}