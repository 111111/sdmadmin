package com.sdmadmin.controller;

import com.github.pagehelper.PageInfo;
import com.sdmadmin.config.SysConfig;
import com.sdmadmin.entity.Goods;
import com.sdmadmin.service.GoodsService;
import com.sdmadmin.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * com.sdmadmin.controller说明:
 * Created by qinyun
 * 18/5/28 16:42
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    private Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private SysConfig sysConfig;

    @Autowired
    private GoodsService goodsService;


    @RequestMapping("/importpage")
    public String importPage(String msg, Model model){

        model.addAttribute("title", "导入数据");
        String filedir = sysConfig.getFileDir();
        File fdir = new File(filedir);
        if(!fdir.exists()){
            fdir.mkdirs();
        }
        File[] files = null;

        if(fdir.isDirectory()){
            files = fdir.listFiles((dir, name) -> name.endsWith(".xls") || name.endsWith(".xlsx"));
        }

        List<String> fileNameList = new ArrayList<>();
        if(files != null && files.length > 0){
            for(File f : files){
                fileNameList.add(f.getName());
            }
        }

        model.addAttribute("fileNameList", fileNameList);
        model.addAttribute("msg", msg);
        return "goods/importgoods";
    }

    @RequestMapping("/goodslist")
    public String goodslist(String searchName, Integer pi, Model model){
        if(pi == null || pi < 1){
            pi = 1;
        }
        if(searchName == null){
            searchName = "";
        }

        List<Goods> goodsList = goodsService.getGoodPage(searchName, pi, 20);
        PageInfo<Goods> pageInfo = new PageInfo<Goods>(goodsList, 10);
        model.addAttribute("searchName", searchName);
        model.addAttribute("goodsList", goodsList);
        model.addAttribute("pageinfo", pageInfo);
        model.addAttribute("pi", pi);

        return "goods/goodslist";
    }

    @RequestMapping("/updateGoodsCategory")
    @ResponseBody
    public String updateGoodsCategory(){
        goodsService.updateGoodsCategory();
        return "success";
    }

    @RequestMapping("/importgoods")
    public String importGoods(@RequestParam("uploadFile") MultipartFile file, RedirectAttributes redirectModel){

        // 获取文件名
        String fileName = file.getOriginalFilename();
        logger.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        logger.info("上传的后缀名为：" + suffixName);
        // 文件上传后的路径
        String filePath = sysConfig.getUploadPath();

        String newFileName = UUID.randomUUID() + suffixName;

        try{
            FileUtil.uploadFile(file.getBytes(), filePath, newFileName);
        }catch(Exception ex){
            ex.printStackTrace();
        }

        goodsService.importExcel(filePath, newFileName);
        redirectModel.addAttribute("msg", "后台已异步执行导入操作。");
        return "redirect:/goods/importpage";
    }


    @RequestMapping("/importgoodsfile")
    @ResponseBody
    public String importGoodsFile(String fileName, RedirectAttributes redirectModel){

        logger.info("fileName：{}" + fileName);
        if(fileName != null && !"".equals(fileName)){
            String filedir = sysConfig.getFileDir();

            File srcFile = new File(filedir + fileName);
            if(srcFile.exists()){
                logger.info("filePath：{}" , srcFile.getAbsolutePath());

                goodsService.importExcel(srcFile);
                return "后台已执行导入.";
            }else{
                return "file is null";
            }

        }else{
            return "fileName is null";
        }

    }

}
