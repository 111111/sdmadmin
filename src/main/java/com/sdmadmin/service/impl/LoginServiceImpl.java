package com.sdmadmin.service.impl;

import com.sdmadmin.dao.AdminMapper;
import com.sdmadmin.entity.Admin;
import com.sdmadmin.service.LoginService;
import com.sdmadmin.util.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * com.sdmadmin.service.impl说明:
 * Created by qinyun
 * 2018/6/7 12:43
 */
@Service
public class LoginServiceImpl implements LoginService {

    private Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private AdminMapper adminMapper;

    public Map login(String adminname, String adminPassword){
        Map reMap = new HashMap();
        reMap.put("recode", -1);//登录失败
        reMap.put("message", "登录失败");

        if(adminname == null || "".equals(adminname) || adminPassword == null || "".equals(adminPassword)){
            reMap.put("recode", -1);//用户名或者密码为空登录失败
            reMap.put("message", "用户名或者密码为空，登录失败");
            return reMap;
        }
        Admin admin = adminMapper.selectByUserName(adminname);
        if(admin == null){
            reMap.put("recode", -2);//找不到该用户
            reMap.put("message", "找不到该用户");
            return reMap;
        }

        //MD5加密密码
        try {
            adminPassword = MD5Utils.md5(MD5Utils.md5Half(adminPassword));
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        if(!adminPassword.equals(admin.getPassword())){
            reMap.put("recode", -3);//密码错误
            reMap.put("message", "密码错误");
            return reMap;
        }

        if(admin.getStatus() != 1){
            reMap.put("recode", -4);//该账户已经被禁用
            reMap.put("message", "该账户已经被禁用");
            return reMap;
        }

        reMap.put("admin", admin);
        reMap.put("recode", 1);//登录成功
        reMap.put("message", "登录成功");

        return reMap;
    }
}
