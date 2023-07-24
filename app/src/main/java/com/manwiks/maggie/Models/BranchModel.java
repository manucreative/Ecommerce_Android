package com.manwiks.maggie.Models;

public class BranchModel {

    public BranchModel() {
        ///empty constructor///
    }

    private String branch_img, branch_name, branch_email, branch_phone, branch_address, town;

    public BranchModel(String branch_img, String branch_name, String branch_email, String branch_phone, String branch_address, String town) {
        this.branch_img = branch_img;
        this.branch_name = branch_name;
        this.branch_email = branch_email;
        this.branch_phone = branch_phone;
        this.branch_address = branch_address;
        this.town = town;
    }

    public String getBranch_img() {
        return branch_img;
    }

    public void setBranch_img(String branch_img) {
        this.branch_img = branch_img;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getBranch_email() {
        return branch_email;
    }

    public void setBranch_email(String branch_email) {
        this.branch_email = branch_email;
    }

    public String getBranch_phone() {
        return branch_phone;
    }

    public void setBranch_phone(String branch_phone) {
        this.branch_phone = branch_phone;
    }

    public String getBranch_address() {
        return branch_address;
    }

    public void setBranch_address(String branch_address) {
        this.branch_address = branch_address;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}