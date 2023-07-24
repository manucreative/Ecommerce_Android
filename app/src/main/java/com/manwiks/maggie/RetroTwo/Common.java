package com.manwiks.maggie.RetroTwo;

import com.manwiks.maggie.Database.DataSource.CartRepository;
import com.manwiks.maggie.Database.DataSource.FavoriteRepository;
import com.manwiks.maggie.Database.Local.MaggieRoomDatabase;
import com.manwiks.maggie.Models.CategoryModel;
import com.manwiks.maggie.Models.NewProductsModel;
import com.manwiks.maggie.Models.RegionsModel;
import com.manwiks.maggie.Models.TopUpsModel;
import com.manwiks.maggie.Retrofit.GroceryShopAPI;
import com.manwiks.maggie.Retrofit.RetrofitClient;

public class Common {
    private static final String BASE_URL = "http://192.168.43.219/ecom/";

    public static CategoryModel currentCategory = null;
    public  static NewProductsModel currentProduct = null;
    public static RegionsModel regionsModel = null;
    public static String branches = ""; //not chosen (error)
    public  static TopUpsModel foodTopUps = null;
    public static MaggieRoomDatabase maggieRoomDatabase;
    public static CartRepository cartRepository;
    public static FavoriteRepository favoriteRepository;
    public static GroceryShopAPI getAPI(){
      return RetrofitClient.getClient(BASE_URL).create(GroceryShopAPI.class);
    }
}
