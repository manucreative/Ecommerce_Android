package com.manwiks.maggie.OperationRetrofit;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    //EMAIL
    @GET("users/email_registration.php")
    Call<Users> performEmailRegistration(
            @Query("user_name") String user_name,
            @Query("user_email") String user_email,
            @Query("user_password") String user_password
    );

//EMAIL LOGIN
    @GET("users/email_login.php")
    Call<Users> performEmailLogin(
            @Query("user_email")String user_email,
            @Query("user_password")String user_password
    );

    //PHONE REGISTRATION
    @GET("users/phone_registration.php")
    Call<Users> performPhoneRegistration(

            @Query("user_phone")String user_phone
    );

    //PHONE LOGIN
    @GET("users/phone_login.php")
    Call<Users> performPhoneLoin(

            @Query("user_phone")String user_phone
    );

    //getting all categories
    @GET("api/categories.php")
    Call<Users> getCategory();

    //getting all banners
    @GET("api/bannerapi.php")
    Call<Users> getBanner();

    ////get strips///
    @GET("api/strip.php")
    Call<Users> getStripBanners();

    ////getting branches///
    @GET("api/branchapi.php")
    Call<Users> getBranches();

}
