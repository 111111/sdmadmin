package com.sdmadmin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sdmadmin.dao.CategoryMapper;
import com.sdmadmin.dao.CouponMapper;
import com.sdmadmin.dao.GoodsCategoryMapper;
import com.sdmadmin.dao.GoodsMapper;
import com.sdmadmin.entity.Category;
import com.sdmadmin.entity.Coupon;
import com.sdmadmin.entity.Goods;
import com.sdmadmin.entity.GoodsCategory;
import com.sdmadmin.service.GoodsService;
import com.sdmadmin.util.DateUtil;
import com.sdmadmin.util.ExcelUtil;
import com.sdmadmin.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * com.sdmadmin.service.impl说明:
 * Created by qinyun
 * 18/5/28 16:03
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    private Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    /**
     *
     * @param pi
     * @param ps
     * @return
     */
    @Override
    public List<Goods> getGoodPage(int pi, int ps){
        Page<Goods> page = PageHelper.startPage(pi, ps, true);
        Goods paramGoods = new Goods();
        List<Goods> goodsList = goodsMapper.selectList(paramGoods);
        return goodsList;
    }

    @Override
    public List<Goods> getGoodPage(String searchName, int pi, int ps){
        Page<Goods> page = PageHelper.startPage(pi, ps, true);
        Goods paramGoods = new Goods();
        paramGoods.setGoodname(searchName);
        List<Goods> goodsList = goodsMapper.selectList(paramGoods);
        return goodsList;
    }

    @Override
    @Async
    public void updateVoucherPrice(){
        int pi = 1;
        int pages = 5;

        while (pi <= pages){
            logger.info("pages={},pi={}", pages, pi);
            Page<Goods> page = PageHelper.startPage(pi, 200, true);
            Goods paramGoods = new Goods();
            List<Goods> goodsList = goodsMapper.selectList(paramGoods);
            PageInfo<Goods> pageInfo = new PageInfo<Goods>(goodsList);
            for(Goods goods : goodsList){
                Long goodsid = goods.getId();
                Coupon coupon = couponMapper.selectOneByGoodsId(goodsid);
                BigDecimal voucherPrice = BigDecimal.valueOf(0);
                BigDecimal price = goods.getPrice();
                Integer preferentialAmount = 0;
                if(coupon != null){
                    preferentialAmount = coupon.getPreferentialAmount();

                    voucherPrice = price.subtract(new BigDecimal(preferentialAmount));

                }
                if(voucherPrice.compareTo(BigDecimal.valueOf(0)) <= 0){
                    voucherPrice = price;
                }
                goods.setVoucherPrice(voucherPrice);


                logger.info("goodsid={}, goodsname={},price={},preferentialAmount={},voucherPrice={}", goods.getId(), goods.getGoodname(), price, preferentialAmount, voucherPrice);
                goodsMapper.updateByPrimaryKeySelective(goods);


            }
            pages = pageInfo.getPages();
            pi++;
        }
    }

    @Override
    @Async
    public void updateGoodsCategory(){
        int pi = 1;
        int pages = 5;

        while (pi <= pages){
            logger.info("pages={},pi={}", pages, pi);
            Page<Goods> page = PageHelper.startPage(pi, 200, true);
            Goods paramGoods = new Goods();
            List<Goods> goodsList = goodsMapper.selectList(paramGoods);
            PageInfo<Goods> pageInfo = new PageInfo<Goods>(goodsList);
            for(Goods goods : goodsList){

                String c = goods.getCategory();
                String cids = goods.getCategoryIds();
                if(cids == null){
                    cids = "";
                }
                String[] categoryNames = c.split("/");

                List<Integer> categoryIdList = saveCategory(goods.getId(), categoryNames);
                StringBuilder catgoryIds = new StringBuilder(cids);
                for(Integer cid : categoryIdList){
                    String temp = "|" + cid + "|";
                    if(cids == null || !cids.contains(temp)){
                        catgoryIds.append(temp).append(",");
                    }
                }
                goods.setCategoryIds(catgoryIds.toString());
                goodsMapper.updateByPrimaryKeySelective(goods);
                logger.info("goodsid={}, goodsname={},category={},catgoryIds={}", goods.getId(), goods.getGoodname(), goods.getCategory(), catgoryIds);

            }
            pages = pageInfo.getPages();
            pi++;
        }

    }

    @Override
    @Async
    public void import618Excel(File excelFile){
        List<List<Object>>  objList = null;
        try{
            objList = ExcelUtil.readExcel(excelFile);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("import618Excel error:", ex.getMessage());
        }
        if(objList != null && objList.size() > 0){
            for(int i = 1;i < objList.size(); i++){
                List<Object> rowList = objList.get(i);
//                logger.info("rowList{} = {}" , i, rowList.size());
                Goods goods = row2Goods618(rowList);
                String couponName = goods.getCouponname();


                if("无".equals(couponName)) {
                    couponName = null;
                    goods.setCouponname(couponName);
                }
                Goods goods_temp = goodsMapper.selectByTbId(goods.getTbid());
                if(goods_temp == null){
                    goods.setCreatetime(new Date());
                    goods.setUpdatetime(new Date());
                    goodsMapper.insert(goods);
                }else{
                    goods.setId(goods_temp.getId());
                    goods.setUpdatetime(new Date());
                    goodsMapper.updateByPrimaryKeySelective(goods);
                }


                String c = goods.getCategory();
                String cids = goods.getCategoryIds();
                if(cids == null){
                    cids = "";
                }
                String[] categoryNames = c.split("/");

                List<Integer> categoryIdList = saveCategory(goods.getId(), categoryNames);
                StringBuilder catgoryIds = new StringBuilder(cids);
                for(Integer cid : categoryIdList){
                    String temp = "|" + cid + "|";
                    if(cids == null || !cids.contains(temp)){
                        catgoryIds.append(temp).append(",");
                    }
                }

                goods.setCategoryIds(catgoryIds.toString());
                goodsMapper.updateByPrimaryKeySelective(goods);
                logger.info("rows={}, goodsid={}, goodsname={},category={},catgoryIds={}", i, goods.getId(), goods.getGoodname(), goods.getCategory(), catgoryIds);

            }
        }
    }
    private Goods row2Goods618(List<Object> rowList){
        String a1, b2, c3, d4, e5, f6, g7, h8, i9, j10, k11, l12, m13, n14, o15, p16, q17, r18;
        a1 = (String)rowList.get(0);
        b2 = (String)rowList.get(1);
        c3 = (String)rowList.get(2);
        d4 = (String)rowList.get(3);
        e5 = (String)rowList.get(4);
        f6 = (String)rowList.get(5);
        g7 = (String)rowList.get(6);
        h8 = (String)rowList.get(7);
        i9 = (String)rowList.get(8);
        j10 = (String)rowList.get(9);
        k11 = (String)rowList.get(10);
        l12 = (String)rowList.get(11);
        m13 = (String)rowList.get(12);
        n14 = (String)rowList.get(13);
        o15 = (String)rowList.get(14);
        p16 = (String)rowList.get(15);
        q17 = (String)rowList.get(16);
        r18 = (String)rowList.get(17);

        Goods goods = new Goods();
        goods.setTbid(d4);
        goods.setGoodname(e5);
        goods.setMainpicurl(g7);
        goods.setViewurl(f6);
        goods.setCategory(a1);
        goods.setTbkurl(q17);
        goods.setPrice(new BigDecimal(h8));
        goods.setMonthsales(0);
        goods.setIncomeratio(new BigDecimal(i9));
        goods.setShopname(b2);
        goods.setPlatform(c3);

        goods.setCoupontotal(Integer.parseInt(m13));
        goods.setCouponsurplus(Integer.parseInt(n14));
        goods.setCouponname(k11);
        if(o15 != null && !"".equals(o15)){
            goods.setCouponsstarttime(DateUtil.converDate(o15));
        }
        if(p16 != null && !"".equals(p16)){
            goods.setCouponsendtime(DateUtil.converDate(p16));
        }

        goods.setCouponsurl(q17);
        goods.setCouponsextensionurl(q17);
        goods.setRemarks(r18);

        BigDecimal voucherPrice = goods.getPrice();
        if(l12 != null && !"".equals(l12.trim())){
            voucherPrice = new BigDecimal(l12);
        }

        goods.setVoucherPrice(voucherPrice);
//        System.out.println("goods = " + goods);

        return goods;
    }

    @Override
    @Async
    public void importExcel(String filePath, String fileName){
        File file = new File(filePath + fileName);
        importExcel(file);
    }
    @Override
    @Async
    public void importExcel(File excelFile){

        List<List<Object>>  objList = null;
        try{
            objList = ExcelUtil.readExcel(excelFile);
        }catch(Exception ex){
            ex.printStackTrace();
            logger.error("importExcel error:", ex.getMessage());
        }
        if(objList != null && objList.size() > 0){
            for(int i = 1;i < objList.size(); i++){
                List<Object> rowList = objList.get(i);
//                logger.info("rowList{} = {}" , i, rowList.size());
                Goods goods = row2Goods(rowList);
                Coupon coupon = row2Coupon(rowList);


                Goods goods_temp = goodsMapper.selectByTbId(goods.getTbid());
                if(goods_temp == null){
                    goods.setCreatetime(new Date());
                    goods.setUpdatetime(new Date());
                    goodsMapper.insert(goods);
                }else{
                    goods.setId(goods_temp.getId());
                    goods.setUpdatetime(new Date());
                    goodsMapper.updateByPrimaryKeySelective(goods);
                }


                String c = goods.getCategory();
                String cids = goods.getCategoryIds();
                if(cids == null){
                    cids = "";
                }
                String[] categoryNames = c.split("/");

                List<Integer> categoryIdList = saveCategory(goods.getId(), categoryNames);
                StringBuilder catgoryIds = new StringBuilder(cids);
                for(Integer cid : categoryIdList){
                    String temp = "|" + cid + "|";
                    if(cids == null || !cids.contains(temp)){
                        catgoryIds.append(temp).append(",");
                    }
                }

//                System.out.println("goods = " + goods);
                Coupon coupon_temp = new Coupon();
                coupon_temp.setGoodsid(goods.getId());
                coupon_temp.setCouponid(coupon.getCouponid());
                coupon_temp = couponMapper.selectByGoodsidAndCouponId(coupon_temp);
                if(coupon_temp == null){
                    coupon.setGoodsid(goods.getId());
                    coupon.setCreatetime(new Date());
                    coupon.setUpdatetime(new Date());
                    couponMapper.insert(coupon);
                }else{
                    coupon.setId(coupon_temp.getId());
                    coupon.setGoodsid(goods.getId());
                    coupon.setUpdatetime(new Date());
                    couponMapper.updateByPrimaryKeySelective(coupon);
                }

                BigDecimal price = goods.getPrice();
                Integer preferentialAmount = coupon.getPreferentialAmount();

                BigDecimal voucherPrice = price.subtract(new BigDecimal(preferentialAmount));

                if(voucherPrice.compareTo(BigDecimal.valueOf(0)) <= 0){
                    voucherPrice = price;
                }
                goods.setVoucherPrice(voucherPrice);

                goods.setCategoryIds(catgoryIds.toString());
                goodsMapper.updateByPrimaryKeySelective(goods);
                logger.info("rows={}, goodsid={}, goodsname={},category={},catgoryIds={}", i, goods.getId(), goods.getGoodname(), goods.getCategory(), catgoryIds);

            }
        }

    }

    /**
     *
     * @param rowList
     * @return
     */
    private Goods row2Goods(List<Object> rowList){
        String a1, b2, c3, d4, e5, f6, g7, h8, i9, j10, k11, l12, m13, n14, o15, p16, q17, r18, s19, t20, u21, v22;
        a1 = (String)rowList.get(0);
        b2 = (String)rowList.get(1);
        c3 = (String)rowList.get(2);
        d4 = (String)rowList.get(3);
        e5 = (String)rowList.get(4);
        f6 = (String)rowList.get(5);
        g7 = (String)rowList.get(6);
        h8 = (String)rowList.get(7);
        i9 = (String)rowList.get(8);
        j10 = (String)rowList.get(9);
        k11 = (String)rowList.get(10);
        l12 = (String)rowList.get(11);
        m13 = (String)rowList.get(12);
        n14 = (String)rowList.get(13);
        o15 = (String)rowList.get(14);
        p16 = (String)rowList.get(15);
        q17 = (String)rowList.get(16);
        r18 = (String)rowList.get(17);
        s19 = (String)rowList.get(18);
        t20 = (String)rowList.get(19);
        u21 = (String)rowList.get(20);
        v22 = (String)rowList.get(21);
//        logger.info("rowList:a1 = {},b2 = {},c3 = {},d4 = {},e5 = {},f6 = {},g7 = {},h8 = {},i9 = {},j10 = {},k11 = {},l12 = {},m13 = {},n14 = {},o15 = {},p16 = {},,q17 = {},r18 = {},s19 = {},t20 = {},u21 = {},v22 = {}"
//                , a1, b2, c3, d4, e5, f6, g7, h8, i9, j10,k11, l12, m13, n14, o15, p16, q17, r18, s19, t20, u21, v22);
        Goods goods = new Goods();
        goods.setTbid(a1);
        goods.setGoodname(b2);
        goods.setMainpicurl(c3);
        goods.setViewurl(d4);
        goods.setCategory(e5);
        goods.setTbkurl(f6);
        goods.setPrice(new BigDecimal(g7));
        goods.setMonthsales(Integer.parseInt(h8));
        goods.setIncomeratio(new BigDecimal(i9));
        goods.setCommision(new BigDecimal(j10));
        goods.setSellername(k11);
        goods.setSellerid(l12);
        goods.setShopname(m13);
        goods.setPlatform(n14);

        goods.setCouponid(o15);
        goods.setCoupontotal(Integer.parseInt(p16));
        goods.setCouponsurplus(Integer.parseInt(q17));
        goods.setCouponname(r18);
        goods.setCouponsstarttime(DateUtil.converDate(s19));
        goods.setCouponsendtime(DateUtil.converDate(t20));
        goods.setCouponsurl(u21);
        goods.setCouponsextensionurl(v22);
//        System.out.println("goods = " + goods);

        return goods;
    }

    /**
     *
     * @param rowList
     * @return
     */
    private Coupon row2Coupon(List<Object> rowList){
        Coupon coupon = new Coupon();

        String a1, b2, c3, d4, e5, f6, g7, h8, i9, j10, k11, l12, m13, n14, o15, p16, q17, r18, s19, t20, u21, v22;
        a1 = (String)rowList.get(0);
        o15 = (String)rowList.get(14);
        p16 = (String)rowList.get(15);
        q17 = (String)rowList.get(16);
        r18 = (String)rowList.get(17);
        s19 = (String)rowList.get(18);
        t20 = (String)rowList.get(19);
        u21 = (String)rowList.get(20);
        v22 = (String)rowList.get(21);



        coupon.setTbid(a1);
        coupon.setCouponid(o15);
        coupon.setCoupontotal(Integer.parseInt(p16));
        coupon.setCouponsurplus(Integer.parseInt(q17));
        coupon.setCouponname(r18);
        coupon.setCouponsstarttime(DateUtil.converDate(s19));
        coupon.setCouponsendtime(DateUtil.converDate(t20));
        coupon.setCouponsurl(u21);
        coupon.setCouponsextensionurl(v22);
        coupon.setCreatetime(new Date());

        int fullAmount = couponname2fullAmount(r18);
        int preferentialAmount = couponname2preferentialAmount(r18);

        coupon.setFullAmount(fullAmount);
        coupon.setPreferentialAmount(preferentialAmount);

        return coupon;
    }

    /**
     *
     * @param couponname
     * @return
     */
    public Integer couponname2fullAmount(String couponname){
        int fullAmount = 0;
        if(couponname.startsWith("满")){
            String fullAmountStr = couponname.substring(couponname.indexOf("满") + 1, couponname.indexOf("元"));
            fullAmount = StringUtils.nullToInteger(fullAmountStr, 0);
        }
        return fullAmount;
    }

    /**
     *
     * @param couponname
     * @return
     */
    public Integer couponname2preferentialAmount(String couponname){
        int preferentialAmount = 0;
        if(couponname.startsWith("满")){
            //满10元减5元
            String preferentialAmountStr = couponname.substring(couponname.indexOf("减") + 1, couponname.lastIndexOf("元"));
            preferentialAmount = StringUtils.nullToInteger(preferentialAmountStr, 0);

        }else{
            //3元无条件券
            String preferentialAmountStr = couponname.substring(0, couponname.lastIndexOf("元"));
            preferentialAmount = StringUtils.nullToInteger(preferentialAmountStr, 0);
        }
       return preferentialAmount;
    }

    private List<Integer> saveCategory(Long goodsId, String[] categoryNames){
        List<Integer> reList = new ArrayList();
        for(String cName : categoryNames){
            Category category = saveCategory(0, goodsId, cName);
            reList.add(category.getId());

        }
        return reList;
    }

    /**
     *
     * @param parentid
     * @param categoryName
     * @return
     */
    private Category saveCategory(Integer parentid, Long goodsId, String categoryName){
        Category category = new Category();
        category.setParentid(parentid);
        category.setCategoryname(categoryName);
        category = categoryMapper.selectByParameter(category);
        if(category == null){
            category = new Category();
            category.setParentid(parentid);
            category.setCategoryname(categoryName);
            category.setGoodsNum(1);
            categoryMapper.insertSelective(category);
        }

        GoodsCategory goodsCategory = new GoodsCategory();
        goodsCategory.setGoodsid(goodsId);
        goodsCategory.setCategoryid(category.getId());
        goodsCategory = goodsCategoryMapper.selectByParameter(goodsCategory);
        if(goodsCategory == null){
            goodsCategory = new GoodsCategory();
            goodsCategory.setGoodsid(goodsId);
            goodsCategory.setCategoryid(category.getId());
            goodsCategory.setCreatetime(new Date());
            goodsCategoryMapper.insert(goodsCategory);

        }
        return category;

    }
}
