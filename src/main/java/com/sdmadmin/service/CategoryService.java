package com.sdmadmin.service;

import com.github.pagehelper.PageInfo;
import com.sdmadmin.entity.Category;

/**
 * com.sdmadmin.service说明:
 * Created by qinyun
 * 2018/6/12 10:51
 */
public interface CategoryService {

    PageInfo<Category> findCategoryPage(int pi, int ps);
}
