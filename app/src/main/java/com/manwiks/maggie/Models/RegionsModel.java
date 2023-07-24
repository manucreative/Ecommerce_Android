package com.manwiks.maggie.Models;

public class RegionsModel {
    public RegionsModel() {
        /////empty constructor
    }

    public String code, default_name;

    public RegionsModel(String code, String default_name) {
        this.code = code;
        this.default_name = default_name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDefault_name() {
        return default_name;
    }

    public void setDefault_name(String default_name) {
        this.default_name = default_name;
    }
}
