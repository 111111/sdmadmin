package com.sdmadmin.controller;

import com.github.pagehelper.PageInfo;
import com.sdmadmin.entity.Coupon;
import com.sdmadmin.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * com.sdmadmin.controller说明:
 * Created by qinyun
 * 2018/6/11 23:07
 */
@Controller
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @RequestMapping("/couponlist")
    public String couponList(Integer pi, Model model){
        if(pi == null || pi < 1){
            pi = 1;
        }

        PageInfo<Coupon> pageInfo = couponService.findCouponPageList(pi,20);

        model.addAttribute("pageinfo", pageInfo);
        model.addAttribute("pi", pi);
        return "coupon/couponlist";
    }
}
