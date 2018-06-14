package com.sdmadmin.service.impl;

import com.sdmadmin.dao.UserMapper;
import com.sdmadmin.entity.User;
import com.sdmadmin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * com.sdmadmin.service.impl说明:
 * Created by qinyun
 * 18/5/24 16:14
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> listAllUser(){
        return userMapper.selectAll();
    }
}
