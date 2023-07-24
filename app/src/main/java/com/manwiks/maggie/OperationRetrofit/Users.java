package com.manwiks.maggie.OperationRetrofit;

import com.google.gson.annotations.SerializedName;
import com.manwiks.maggie.Models.BannerModel;
import com.manwiks.maggie.Models.BranchModel;
import com.manwiks.maggie.Models.CategoryModel;

import java.util.List;

public class Users {

    @SerializedName("response")
    private  String Response;

    @SerializedName("user_id")
    private String UserId;

    @SerializedName("strip_image")
    private String strip_image;

    @SerializedName("categories")
    private List<CategoryModel> category;

    @SerializedName("banners")
    private List<BannerModel> banner;

    @SerializedName("branches")
    private List<BranchModel> branch;


    public String getResponse() {
        return Response;
    }

    public String getUserId() {
        return UserId;
    }

    public String getStrip_image() {
        return strip_image;
    }

    public List<CategoryModel> getCategory() {
        return category;
    }

    public List<BannerModel> getBanner() {
        return banner;
    }

    public List<BranchModel> getBranch() {
        return branch;
    }

}
