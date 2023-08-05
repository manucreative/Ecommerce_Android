package com.manwiks.maggie;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.gson.Gson;
import com.manwiks.maggie.Adapters.ICartAdapter;
import com.manwiks.maggie.Database.Local.CartDAO;
import com.manwiks.maggie.Database.ModelDB.Cart;
import com.manwiks.maggie.EmailLogInRegister.EmailLogInActivity;
import com.manwiks.maggie.Models.RegionsModel;
import com.manwiks.maggie.RetroTwo.Common;
import com.manwiks.maggie.Retrofit.GroceryShopAPI;

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

public class CartActivity extends AppCompatActivity {

    RecyclerView recycler_cart;
    Button btn_plce_order;
    Spinner select_region;

    CompositeDisposable compositeDisposable;
    ArrayAdapter<String> regionsAdapter;
    List<RegionsModel> regions;
    List<String> defaultRegionNames;

    private RegionsModel selectedRegions;


    GroceryShopAPI mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

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
}