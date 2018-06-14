package com.sdmadmin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sdmadmin.dao.CouponMapper;
import com.sdmadmin.entity.Coupon;
import com.sdmadmin.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * com.sdmadmin.service.impl说明:
 * Created by qinyun
 * 2018/6/11 23:08
 */
@Service
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    public PageInfo<Coupon> findCouponPageList(int pi, int ps){
        Page<Coupon> page = PageHelper.startPage(pi, ps, true);

        Coupon param = new Coupon();
        List<Coupon>  couponList = couponMapper.selectByParamter(param);
        PageInfo<Coupon> pageInfo = new PageInfo<Coupon>(couponList);
        return pageInfo;


    }
}
