package com.sdmadmin.controller;

import com.sdmadmin.entity.User;
import com.sdmadmin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * com.sdmadmin.controller说明:
 * Created by qinyun
 * 18/5/24 16:11
 */
@Controller
public class HomeController {

    @Autowired
    private UserService userService;


    @RequestMapping("/")
    public String home() {

        return "redirect:login/init";
    }

    @RequestMapping("/home/index")
    public String index() {

        return "home/index";
    }
}
