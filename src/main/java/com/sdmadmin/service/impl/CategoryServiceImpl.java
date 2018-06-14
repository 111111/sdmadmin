package com.sdmadmin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sdmadmin.dao.CategoryMapper;
import com.sdmadmin.entity.Category;
import com.sdmadmin.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * com.sdmadmin.service.impl说明:
 * Created by qinyun
 * 2018/6/12 10:52
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public PageInfo<Category> findCategoryPage(int pi, int ps){

        Page<Category> page = PageHelper.startPage(pi, ps, true);

        Category param = new Category();
        List<Category>  categoryList = categoryMapper.selectListByParameter(param);

        PageInfo<Category> pageInfo = new PageInfo<Category>(categoryList);
        return pageInfo;

    }

}
