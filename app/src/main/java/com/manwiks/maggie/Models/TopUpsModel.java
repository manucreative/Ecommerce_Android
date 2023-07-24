package com.manwiks.maggie.Models;

import com.google.gson.Gson;

public class TopUpsModel {

    public String top_up_id;
    public String top_up_name;
    public String top_up_price;
    public boolean isChecked;
    public  int btnNumber;

    public TopUpsModel(String top_up_id, String top_up_name, String top_up_price, boolean isChecked, String btnNumber) {
        this.top_up_id = top_up_id;
        this.top_up_name = top_up_name;
        this.top_up_price = top_up_price;
        this.isChecked = isChecked;
        this.btnNumber = Integer.parseInt(btnNumber);
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

    public String getTop_up_price() {
        return top_up_price;
    }

    public void setTop_up_price(String top_up_price) {
        this.top_up_price = top_up_price;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getBtnNumber() {
        return btnNumber;
    }

    public void setBtnNumber(int btnNumber) {
        this.btnNumber = btnNumber;
    }
    // Convert object to JSON string
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}

