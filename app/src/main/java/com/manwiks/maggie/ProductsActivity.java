package com.manwiks.maggie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.manwiks.maggie.Adapters.ProductsAdapter;
import com.manwiks.maggie.Models.NewProductsModel;
import com.manwiks.maggie.Models.RegionsModel;
import com.manwiks.maggie.RetroTwo.Common;
import com.manwiks.maggie.Retrofit.GroceryShopAPI;
import com.manwiks.maggie.Sessions.SessionManager;

import java.util.List;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProductsActivity extends AppCompatActivity {

    //////////vegetables start////////
    private RecyclerView recyclerProducts;
    private List<NewProductsModel> newProductsModelList;
    private List<RegionsModel> regionsModelList;
    ///////vegetables end////////////
    TextView cat_name;
    ImageView cat_image;
    GroceryShopAPI mService;
    private FloatingActionButton float_cart;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

//    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

//        sessionManager = new SessionManager(this);
        mService = Common.getAPI();

        cat_name = (TextView) findViewById(R.id.catTitle);
        cat_image = (ImageView) findViewById(R.id.catImg);

        TextView txtCartIcon = findViewById(R.id.cart_qty);
        Common.cartRepository.getCartItemsLiveData().observe(this, cartItems->{
            int totalQty = Common.cartRepository.getCountItems();
            txtCartIcon.setText(String.valueOf(totalQty));
        });
        float_cart = findViewById(R.id.icon_cart);
        float_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsActivity.this, CartActivity.class);
                startActivity(intent);
                Animatoo.animateFade(ProductsActivity.this);
            }
        });

        Glide.with(this).load(Common.currentCategory.getCat_image()).placeholder(R.drawable.two).into(cat_image);
        cat_name.setText(Common.currentCategory.getCat_title());

        recyclerProducts = (RecyclerView)findViewById(R.id.recyclerProducts);
        int numberOfColumns = 2;
        recyclerProducts.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        init(Common.currentCategory.getCat_id());

    }

    private void init(String catId) {
       compositeDisposable.add(mService.getProducts(catId)
       .subscribeOn(Schedulers.io())
       .observeOn(AndroidSchedulers.mainThread())
       .subscribe(this::displayVegesList));

    }

    private void displayVegesList(List<NewProductsModel> newProductsModelList) {
           ProductsAdapter productsAdapter = new ProductsAdapter(this, newProductsModelList);
        recyclerProducts.setAdapter(productsAdapter);
            productsAdapter.notifyDataSetChanged();

    }


}