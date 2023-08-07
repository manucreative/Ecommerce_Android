package com.manwiks.maggie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.manwiks.maggie.Adapters.ProductsAdapter;
import com.manwiks.maggie.ManageDrawer.AboutUs;
import com.manwiks.maggie.ManageDrawer.ContactUs;
import com.manwiks.maggie.ManageDrawer.CustomerFavorites;
import com.manwiks.maggie.ManageDrawer.CustomerFeedback;
import com.manwiks.maggie.ManageDrawer.CustomerOders;
import com.manwiks.maggie.ManageDrawer.OrderHelp;
import com.manwiks.maggie.ManageDrawer.RateUs;
import com.manwiks.maggie.ManageDrawer.ReportEmergency;
import com.manwiks.maggie.ManageDrawer.UserProfile;
import com.manwiks.maggie.Models.NewProductsModel;
import com.manwiks.maggie.Models.RegionsModel;
import com.manwiks.maggie.RetroTwo.Common;
import com.manwiks.maggie.Retrofit.GroceryShopAPI;
import com.manwiks.maggie.Sessions.SessionManager;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;
import java.util.Objects;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProductsActivity extends AppCompatActivity implements View.OnClickListener {

    //////////vegetables start////////
    private RecyclerView recyclerProducts;
    private List<NewProductsModel> newProductsModelList;
    private List<RegionsModel> regionsModelList;
    ///////vegetables end////////////
    TextView cat_name;
    ImageView cat_image;
    GroceryShopAPI mService;
    private FloatingActionButton float_cart;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView navigationBar;
    private RelativeLayout  relativeLayout3Bookmarks, relativeLayout4Earnings;
    private TextView your_orders, favorite_orders, address, online_order_help, send_feedback, report_safety, rate_us, contact_us, logout;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

//    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        // back Press for top part
        ImageView backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Call the default back behavior
            }
        });
        // back Press for top part end
        // Data Side bar Expandable
        ExpandableLayout expandableLayout = (ExpandableLayout) findViewById(R.id.expandableLayout);
        ImageView rightArrow = (ImageView) findViewById(R.id.right_arrow);
        RelativeLayout titleLayout = (RelativeLayout) findViewById(R.id.myRelativeLayout);
        titleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableLayout.toggle();
                rightArrow.setRotation(expandableLayout.isExpanded() ? 90 : 0);
            }
        });
        // Data Side bar Expandable end

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
        int portraitColumns = 2; // Default number of columns for portrait orientation
        int landscapeColumns = 3; // Default number of columns for landscape orientation
        int tabletPortraitColumns = 4; // Number of columns for tablets in portrait
        int tabletLandscapeColumns = 5; // Number of columns for tablets in landscape
        int numberOfColumns;
        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if(screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE){
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                numberOfColumns = tabletLandscapeColumns;
            }else {
                numberOfColumns = tabletPortraitColumns;
            }
        }else {
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                numberOfColumns =landscapeColumns;
            }else {
                numberOfColumns = portraitColumns;
            }
        }
        recyclerProducts.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        init(Common.currentCategory.getCat_id());
        onSetNavigationDrawerEvents();
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

    private void onSetNavigationDrawerEvents() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationBar = (ImageView) findViewById(R.id.navigationBar);
        //    add_to_cart = (ImageView)view.findViewById(R.id.icon_cart);



        logout = (TextView) findViewById(R.id.logOut);
        relativeLayout3Bookmarks = (RelativeLayout) findViewById(R.id.relativeLayout3);
        relativeLayout4Earnings = (RelativeLayout) findViewById(R.id.relativeLayout4);

        your_orders = (TextView) findViewById(R.id.your_orders);
        favorite_orders = (TextView) findViewById(R.id.favorite_orders);
        address = (TextView) findViewById(R.id.address);
        online_order_help = (TextView) findViewById(R.id.online_order_help);
        send_feedback = (TextView) findViewById(R.id.send_feedback);
        report_safety = (TextView) findViewById(R.id.report_safety);
        rate_us = (TextView) findViewById(R.id.rate_us);
        contact_us = (TextView) findViewById(R.id.contact_us);

//        add_to_cart.setOnClickListener(this);
        navigationBar.setOnClickListener(this);
        logout.setOnClickListener(this);
        relativeLayout3Bookmarks.setOnClickListener(this);
        relativeLayout4Earnings.setOnClickListener(this);

        your_orders.setOnClickListener(this);
        favorite_orders.setOnClickListener(this);
        address.setOnClickListener(this);
        online_order_help.setOnClickListener(this);
        send_feedback.setOnClickListener(this);
        report_safety.setOnClickListener(this);
        rate_us.setOnClickListener(this);
        contact_us.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navigationBar:
                drawerLayout.openDrawer(navigationView, true);
                break;
            case R.id.logOut:
                //logOut();
                break;
            case R.id.relativeLayout3:
                Intent intent1 = new Intent(ProductsActivity.this, AboutUs.class);
                startActivity(intent1);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.relativeLayout4:
                Toast.makeText(this, "Earnings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.your_orders:
                Intent intent2 = new Intent(ProductsActivity.this, CustomerOders.class);
                startActivity(intent2);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.favorite_orders:
                Intent intent3 = new Intent(ProductsActivity.this, CustomerFavorites.class);
                startActivity(intent3);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.address:
                Intent intent4 = new Intent(ProductsActivity.this, UserProfile.class);
                startActivity(intent4);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.online_order_help:
                Intent intent5 = new Intent(ProductsActivity.this, OrderHelp.class);
                startActivity(intent5);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.send_feedback:
                Intent intent6 = new Intent(ProductsActivity.this, CustomerFeedback.class);
                startActivity(intent6);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.report_safety:
                Intent intent7 = new Intent(ProductsActivity.this, ReportEmergency.class);
                startActivity(intent7);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.rate_us:
                Intent intent8 = new Intent(ProductsActivity.this, RateUs.class);
                startActivity(intent8);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.contact_us:
                Intent intent9 = new Intent(ProductsActivity.this, ContactUs.class);
                startActivity(intent9);
                finish();
                Animatoo.animateFade(this);
                break;
        }
        }
}