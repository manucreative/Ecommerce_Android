package com.manwiks.maggie.Models;

public class ProductsModel {

    public ProductsModel(){
        ////empty constractor//////
    }
    private int product_img;
    private  String product_name, product_price, product_weigh, cat_add;

    public ProductsModel(int product_img, String product_name, String product_price, String product_weigh, String cat_add) {
        this.product_img = product_img;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_weigh = product_weigh;
        this.cat_add = cat_add;
    }

    public int getProduct_img() {
        return product_img;
    }

    public void setProduct_img(int product_img) {
        this.product_img = product_img;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_weigh() {
        return product_weigh;
    }

    public void setProduct_weigh(String product_weigh) {
        this.product_weigh = product_weigh;
    }

    public String getCat_add() {
        return cat_add;
    }

    public void setCat_add(String cat_add) {
        this.cat_add = cat_add;
    }
}
