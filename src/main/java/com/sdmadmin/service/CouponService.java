package com.sdmadmin.service;

import com.github.pagehelper.PageInfo;
import com.sdmadmin.entity.Coupon;

/**
 * com.sdmadmin.service说明:
 * Created by qinyun
 * 2018/6/11 23:08
 */
public interface CouponService {

    PageInfo<Coupon> findCouponPageList(int pi, int ps);
}
