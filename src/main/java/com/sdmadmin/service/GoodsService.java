package com.sdmadmin.service;

import com.sdmadmin.entity.Goods;

import java.io.File;
import java.util.List;

/**
 * com.sdmadmin.service说明:
 * Created by qinyun
 * 18/5/28 16:01
 */
public interface GoodsService {

    List<Goods> getGoodPage(int pi, int ps);

    List<Goods> getGoodPage(String searchName, int pi, int ps);

    void updateGoodsCategory();

    void importExcel(String filePath, String fileName);

    void importExcel(File excelFile);
}
