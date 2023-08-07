package com.manwiks.maggie;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.manwiks.maggie.Adapters.ICartAdapter;
import com.manwiks.maggie.Database.Local.CartDAO;
import com.manwiks.maggie.Database.ModelDB.Cart;
import com.manwiks.maggie.EmailLogInRegister.EmailLogInActivity;
import com.manwiks.maggie.ManageDrawer.AboutUs;
import com.manwiks.maggie.ManageDrawer.ContactUs;
import com.manwiks.maggie.ManageDrawer.CustomerFavorites;
import com.manwiks.maggie.ManageDrawer.CustomerFeedback;
import com.manwiks.maggie.ManageDrawer.CustomerOders;
import com.manwiks.maggie.ManageDrawer.OrderHelp;
import com.manwiks.maggie.ManageDrawer.RateUs;
import com.manwiks.maggie.ManageDrawer.ReportEmergency;
import com.manwiks.maggie.ManageDrawer.UserProfile;
import com.manwiks.maggie.Models.RegionsModel;
import com.manwiks.maggie.RetroTwo.Common;
import com.manwiks.maggie.Retrofit.GroceryShopAPI;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recycler_cart;
    Button btn_plce_order;
    Spinner select_region;

    CompositeDisposable compositeDisposable;
    ArrayAdapter<String> regionsAdapter;
    List<RegionsModel> regions;
    List<String> defaultRegionNames;

    private RegionsModel selectedRegions;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView navigationBar;
    private RelativeLayout relativeLayout3Bookmarks, relativeLayout4Earnings;
    private TextView your_orders, favorite_orders, address, online_order_help, send_feedback, report_safety, rate_us, contact_us, logout;



    GroceryShopAPI mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

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
        compositeDisposable = new CompositeDisposable();

       mService = Common.getAPI();

        recycler_cart = (RecyclerView)findViewById(R.id.recyler_cart);
        recycler_cart.setLayoutManager(new LinearLayoutManager(this));
        recycler_cart.setHasFixedSize(true);

        btn_plce_order = (Button)findViewById(R.id.btn_place_order);
        TextView txtCartIcon = findViewById(R.id.cart_qty);
        Common.cartRepository.getCartItemsLiveData().observe(this, cartItems->{
            int totalQty = Common.cartRepository.getCountItems();
            txtCartIcon.setText(String.valueOf(totalQty));
        });

        compositeDisposable.add(
                Common.cartRepository.getCartItems()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cartList->{
                            boolean isEmptyCart = cartList.isEmpty();
                            if(isEmptyCart){
                                btn_plce_order.setVisibility(View.INVISIBLE);
                            }else
                                btn_plce_order.setVisibility(View.VISIBLE);
                        }, throwable -> {
                            // handling og other errors
                        })
        );
        btn_plce_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this,Checkout.class);
                startActivity(intent);
                finish();
                Animatoo.animateInAndOut(CartActivity.this);
            }
        });
        compositeDisposable.add(
                Common.cartRepository.getCartItems()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(carts->{
                            boolean isEmptyCart = carts.isEmpty();
                            if(isEmptyCart){
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        View itemView = LayoutInflater.from(this).inflate(R.layout.out_of_cart_layout, null);
                        builder.setView(itemView);
                        builder.setPositiveButton("GO BACK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent myIntent = new Intent(CartActivity.this, HomeActivity.class);
                                myIntent.putExtra("startFragment", "orders");
                                startActivity(myIntent);
                                finish();
                                Animatoo.animateSlideLeft(CartActivity.this);
                            }
                        });
                                AlertDialog dialog = builder.create();
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                            }else {
                                LoadCartItems();
                            }
                        }, throwable -> {
                            // handling og other errors
                        })
        );
        onSetNavigationDrawerEvents();
    }


    private void LoadCartItems() {
        compositeDisposable.add(
                Common.cartRepository.getCartItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::DisplayCartItems)
        );
    }

    private void DisplayCartItems(List<Cart> carts) {
        ICartAdapter cartAdapter = new ICartAdapter(this,carts);
        recycler_cart.setAdapter(cartAdapter);
    }

    //Ctrl + 0

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
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
                Intent intent1 = new Intent(CartActivity.this, AboutUs.class);
                startActivity(intent1);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.relativeLayout4:
                Toast.makeText(this, "Earnings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.your_orders:
                Intent intent2 = new Intent(CartActivity.this, CustomerOders.class);
                startActivity(intent2);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.favorite_orders:
                Intent intent3 = new Intent(CartActivity.this, CustomerFavorites.class);
                startActivity(intent3);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.address:
                Intent intent4 = new Intent(CartActivity.this, UserProfile.class);
                startActivity(intent4);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.online_order_help:
                Intent intent5 = new Intent(CartActivity.this, OrderHelp.class);
                startActivity(intent5);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.send_feedback:
                Intent intent6 = new Intent(CartActivity.this, CustomerFeedback.class);
                startActivity(intent6);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.report_safety:
                Intent intent7 = new Intent(CartActivity.this, ReportEmergency.class);
                startActivity(intent7);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.rate_us:
                Intent intent8 = new Intent(CartActivity.this, RateUs.class);
                startActivity(intent8);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.contact_us:
                Intent intent9 = new Intent(CartActivity.this, ContactUs.class);
                startActivity(intent9);
                finish();
                Animatoo.animateFade(this);
                break;
        }
    }
}