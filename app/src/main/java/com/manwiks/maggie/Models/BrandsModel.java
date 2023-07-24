package com.manwiks.maggie.Models;

public class BrandsModel {

    private String brand_name;
    private boolean checked;
    private int brand_amount;
    private String top_up_id;
    private String top_up_name;

    public BrandsModel(String brand_name, boolean checked, int brand_amount, String top_up_id, String top_up_name) {
        this.brand_name = brand_name;
        this.checked = checked;
        this.brand_amount = brand_amount;
        this.top_up_id = top_up_id;
        this.top_up_name = top_up_name;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getBrand_amount() {
        return brand_amount;
    }

    public void setBrand_amount(int brand_amount) {
        this.brand_amount = brand_amount;
    }

    public String getTop_up_id() {
        return top_up_id;
    }

    public void setTop_up_id(String top_up_id) {
        this.top_up_id = top_up_id;
    }

    public String getTop_up_name() {
        return top_up_name;
    }

    public void setTop_up_name(String top_up_name) {
        this.top_up_name = top_up_name;
    }
}

