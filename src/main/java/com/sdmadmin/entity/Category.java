package com.sdmadmin.entity;

public class Category {
    private Integer id;

    private Integer parentid;

    private String categoryname;

    private Integer goodsNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname == null ? null : categoryname.trim();
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", parentid=" + parentid +
                ", categoryname='" + categoryname + '\'' +
                ", goodsNum=" + goodsNum +
                '}';
    }
}