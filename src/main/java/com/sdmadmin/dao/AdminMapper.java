package com.sdmadmin.dao;

import com.sdmadmin.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Admin record);

    int insertSelective(Admin record);

    Admin selectByPrimaryKey(Integer id);

    Admin selectByUserName(String userName);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);
}