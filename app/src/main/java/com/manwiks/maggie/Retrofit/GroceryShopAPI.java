package com.manwiks.maggie.Retrofit;

import com.manwiks.maggie.Models.BrandsModel;
import com.manwiks.maggie.Models.BrandsTopUpResponse;
import com.manwiks.maggie.Models.NewProductsModel;
import com.manwiks.maggie.Models.RegionsModel;
import com.manwiks.maggie.Models.TopUpsModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GroceryShopAPI {
    ////getting vegetables///
    @FormUrlEncoded
    @POST("api/getProducts.php")
    Observable<List<NewProductsModel>> getProducts(@Field("cat_id") String catId
    );

    @FormUrlEncoded
    @POST("api/submitorder.php")
    Call<String> submitOrder(@Field("price") float OrderPrice,
                             @Field("vat") float TotalTax,
                             @Field("sumPricePlusTax") float SumPricePlusTax,
                             @Field("orderDetails") String OrderDetails,
                             @Field("address") String OrderAddress,
                             @Field("comment") String OrderComments,
                             @Field("phone") String CustomerPhone,
                             @Field("userName") String CustomerName,
                             @Field("userEmail") String CustomerEmail,
                             @Field("selectedDateTime") String CustomerAvailable_time,
                             @Field("region") String CustomerRegion
    );

    @FormUrlEncoded
    @POST("api/getRegionsapi.php")
    Observable<List<RegionsModel>> getRegions(@Field("code") String code,
                                            @Field("default_name") String default_name);

    @FormUrlEncoded
    @POST("api/getTopUpsapi.php")
    Observable<List<TopUpsModel>> getTopUps(@Field("product_id") String productId);

    @FormUrlEncoded
    @POST("api/getBrandsApi.php")
    Observable<List<BrandsModel>> getTopUpBrands(@Field("top_up_id[]") List<String> topUpIds);
}
