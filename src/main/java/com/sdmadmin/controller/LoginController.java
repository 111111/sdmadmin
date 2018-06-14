package com.sdmadmin.controller;

import com.sdmadmin.constant.SysConstant;
import com.sdmadmin.entity.Admin;
import com.sdmadmin.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * com.sdmadmin.controller说明:
 * Created by qinyun
 * 2018/6/7 12:33
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @RequestMapping("/init")
    public String init(){
        return "login";
    }

    @RequestMapping("/dologin")
    public String dologin(HttpServletRequest request, String adminname, String adminPassword, Model model){
        Map reMap = loginService.login(adminname, adminPassword);

        logger.info("dologin reMap = {}", reMap);
        int recode = (Integer)reMap.get("recode");
        String message = (String)reMap.get("message");
        String returnurl = "";
        if (recode == 1) {
            returnurl = "redirect:/home/index";
        } else {
            model.addAttribute("message", message);
            model.addAttribute("adminname", adminname);
            model.addAttribute("adminPassword", adminPassword);
            returnurl = "login";
        }
        Admin admin = (Admin)reMap.get("admin");

        request.getSession().setAttribute(SysConstant.SESSION_LOGIN_KEY, admin);
        return returnurl;
    }

    @RequestMapping("/loginout")
    public String loginOut(HttpServletRequest request){
        request.getSession().setAttribute(SysConstant.SESSION_LOGIN_KEY, null);
        return "redirect:/login/init";
    }
}
