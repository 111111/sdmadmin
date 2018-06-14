package com.sdmadmin.controller;

import com.github.pagehelper.PageInfo;
import com.sdmadmin.entity.Category;
import com.sdmadmin.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * com.sdmadmin.controller说明:
 * Created by qinyun
 * 2018/6/12 10:53
 */
@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/categorylist")
    public String  categoryList(Integer pi, Model model){
        if(pi == null || pi < 1){
            pi = 1;
        }

        PageInfo<Category> pageInfo = categoryService.findCategoryPage(pi, 20);
        model.addAttribute("pageinfo", pageInfo);
        model.addAttribute("pi", pi);
        return "category/categorylist";
    }


    @RequestMapping("/updategoodsnum")
    @ResponseBody
    public String updateGoodsNum(){

        return "";
    }


}
