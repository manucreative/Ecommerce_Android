package com.manwiks.maggie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.manwiks.maggie.Adapters.TopUpsAdapter;
import com.manwiks.maggie.Database.ModelDB.Cart;
import com.manwiks.maggie.ManageDrawer.AboutUs;
import com.manwiks.maggie.ManageDrawer.ContactUs;
import com.manwiks.maggie.ManageDrawer.CustomerFavorites;
import com.manwiks.maggie.ManageDrawer.CustomerFeedback;
import com.manwiks.maggie.ManageDrawer.CustomerOders;
import com.manwiks.maggie.ManageDrawer.OrderHelp;
import com.manwiks.maggie.ManageDrawer.RateUs;
import com.manwiks.maggie.ManageDrawer.ReportEmergency;
import com.manwiks.maggie.ManageDrawer.UserProfile;
import com.manwiks.maggie.Models.TopUpsModel;
import com.manwiks.maggie.RetroTwo.Common;
import com.manwiks.maggie.Retrofit.GroceryShopAPI;
import com.manwiks.maggie.Sessions.SessionManager;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FoodDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView image_food;
    TextView food_name, food_price, food_description, region_test;
    ElegantNumberButton number_button;
    FloatingActionButton btnCart;
    Button addCart;
 ImageView openDrawer;
    GroceryShopAPI mService;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView navigationBar;
    private RelativeLayout relativeLayout3Bookmarks, relativeLayout4Earnings;
    private TextView your_orders, favorite_orders, address, online_order_help, send_feedback, report_safety, rate_us, contact_us, logout;

//    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

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

        openDrawer = findViewById(R.id.navigationBar);
        mService = Common.getAPI();
        image_food = (ImageView) findViewById(R.id.image_food);
        food_name = (TextView) findViewById(R.id.food_name);
        food_price = (TextView) findViewById(R.id.food_price);
        food_description = (TextView) findViewById(R.id.food_description);
        number_button = (ElegantNumberButton) findViewById(R.id.number_button);
        //btnCart = (FloatingActionButton) findViewById(R.id.btnCart);
        addCart = (Button) findViewById(R.id.btnCart);

        TextView txtCartIcon = findViewById(R.id.cart_qty);
        Common.cartRepository.getCartItemsLiveData().observe(this, cartItems-> {
            int totalQty = Common.cartRepository.getCountItems();
            txtCartIcon.setText(String.valueOf(totalQty));
        });
        FloatingActionButton float_cart = findViewById(R.id.icon_cart);
        float_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodDetailsActivity.this, CartActivity.class);
                startActivity(intent);
                Animatoo.animateFade(FoodDetailsActivity.this);
            }
        });

        Glide.with(this).load(Common.currentProduct.link).placeholder(R.drawable.aab).into(image_food);

            food_name.setText(Common.currentProduct.product_name);
            food_price.setText(new StringBuilder("Ksh ").append(Common.currentProduct.product_price));
            food_description.setText(Common.currentProduct.product_desc);

            number_button.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                    double newPrice = Double.parseDouble(Common.currentProduct.product_price) * newValue;
                    food_price.setText(new StringBuilder("Ksh ").append(newPrice));
                }
            });

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddToCartDialog(number_button.getNumber());
            }
        });
//        btnCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showAddToCartDialog(number_button.getNumber());
//            }
//        });
        onSetNavigationDrawerEvents();
    }
@Override
public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
//    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//
//    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//        openDrawer.setVisibility(View.VISIBLE);
//    } else {
//      //  Log.d("OrientationChange", "Unknown orientation");
//    }
}
    public void showAddToCartDialog(String number_button) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View itemView = LayoutInflater.from(this)
                .inflate(R.layout.add_to_cart_layout,null);

// View
        ImageView img_cart_product = (ImageView)itemView.findViewById(R.id.img_cart_product);
        ElegantNumberButton txt_count = (ElegantNumberButton)itemView.findViewById(R.id.txt_count);
        TextView txt_product_dialog = (TextView)itemView.findViewById(R.id.txt_cart_product);
        TextView txt_product_price = (TextView)itemView.findViewById(R.id.cart_price);

        GroceryShopAPI mService;
        mService = Common.getAPI();


        List<TopUpsModel> topUpsModelList = new ArrayList<>();
        TopUpsAdapter adapter = new TopUpsAdapter(topUpsModelList);

        RecyclerView recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerTopUps);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mService.getTopUps(Common.currentProduct.product_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<TopUpsModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<TopUpsModel> topUpsModels) {
                        // Update your adapter with the received data
                        topUpsModelList.clear();
                        topUpsModelList.addAll(topUpsModels);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        Glide.with(this).load(Common.currentProduct.link).placeholder(R.drawable.two).into(img_cart_product);
        txt_product_dialog.setText(Common.currentProduct.product_name);

        Double totalPrice = Double.parseDouble(Common.currentProduct.product_price) * Double.parseDouble(number_button);
        final Double fullPrice = totalPrice;

        txt_product_price.setText(new StringBuilder("Ksh ").append(fullPrice));

        txt_count.setNumber(number_button); // show the selected Number

        txt_count.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                // update the total price based on the new value of the ElegantNumberButton
                double newTotalPrice = Double.parseDouble(Common.currentProduct.product_price) * newValue;
                txt_product_price.setText(new StringBuilder("Ksh ").append(newTotalPrice));
            }
        });
        builder.setView(itemView);
        builder.setPositiveButton("ADD TO CART", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ArrayList<TopUpsModel> selectedProducts = adapter.getSelectedProducts();

                ShowConfirmedDialog(getApplicationContext(),txt_count.getNumber(), selectedProducts);
                dialog.dismiss();
            }
        });


        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
//        builder.show();

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void ShowConfirmedDialog(Context applicationContext, String number, ArrayList<TopUpsModel> selectedProducts) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View itemView = LayoutInflater.from(this)
                .inflate(R.layout.confirm_add_to_cart_layout, null);

        //view
        ImageView img_product = (ImageView) itemView.findViewById(R.id.img_product);
        TextView txt_cart_product_name = (TextView) itemView.findViewById(R.id.txt_cart_product_name);
        TextView txt_product_price = (TextView) itemView.findViewById(R.id.txt_cart_product_price);
        TextView txt_top_ups = (TextView) itemView.findViewById(R.id.txt_top_ups);
        TextView txt_qty = (TextView) itemView.findViewById(R.id.txt_qty);

        TextView top_up_list = (TextView) itemView.findViewById(R.id.top_up_list);

        //setting data
        Glide.with(this).load(Common.currentProduct.link).placeholder(R.drawable.aab).into(img_product);
        txt_cart_product_name.setText(Common.currentProduct.product_name);

        txt_qty.setText(new StringBuilder("X ").append(number));

        String topUpListText = "";
        for (TopUpsModel topUps : selectedProducts) {
            String topUpName = topUps.getTop_up_name();
            String topUpPrice = topUps.getTop_up_price();
            String topUpNumber = String.valueOf(topUps.getBtnNumber());

            Double totalPrice = Double.parseDouble(topUpPrice) * Double.parseDouble(topUpNumber);

            topUpListText += topUpNumber + " " + topUpName + " : Ksh " + String.format("%.2f", totalPrice) + "\n";
        }
        top_up_list.setText(topUpListText);


        txt_top_ups.setText(new StringBuilder("Your Food Top ups").append(Common.branches).toString());

        Double price = (Double.parseDouble(Common.currentProduct.product_price)* Double.parseDouble(number));

        txt_product_price.setText(new StringBuilder("Ksh ").append(price));

        final double finalPrice = price;
        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                //add to SQlite
                //creation of new cart item
                try {
                    Cart cartItem= new Cart();
                    cartItem.name = txt_cart_product_name.getText().toString();
                    cartItem.amount = Integer.parseInt(number);
                    cartItem.price = finalPrice;
                    cartItem.topUps = Common.branches;
                    cartItem.link = Common.currentProduct.link;

                    Common.cartRepository.insertToCart(cartItem);
                    Log.d("Maggie_Debug",new Gson().toJson(cartItem));
                    Toast.makeText(applicationContext, "Save Item to cart succeeded", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(applicationContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });
        builder.setView(itemView);
//        builder.show();

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
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
                Intent intent1 = new Intent(FoodDetailsActivity.this, AboutUs.class);
                startActivity(intent1);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.relativeLayout4:
                Toast.makeText(this, "Earnings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.your_orders:
                Intent intent2 = new Intent(FoodDetailsActivity.this, CustomerOders.class);
                startActivity(intent2);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.favorite_orders:
                Intent intent3 = new Intent(FoodDetailsActivity.this, CustomerFavorites.class);
                startActivity(intent3);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.address:
                Intent intent4 = new Intent(FoodDetailsActivity.this, UserProfile.class);
                startActivity(intent4);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.online_order_help:
                Intent intent5 = new Intent(FoodDetailsActivity.this, OrderHelp.class);
                startActivity(intent5);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.send_feedback:
                Intent intent6 = new Intent(FoodDetailsActivity.this, CustomerFeedback.class);
                startActivity(intent6);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.report_safety:
                Intent intent7 = new Intent(FoodDetailsActivity.this, ReportEmergency.class);
                startActivity(intent7);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.rate_us:
                Intent intent8 = new Intent(FoodDetailsActivity.this, RateUs.class);
                startActivity(intent8);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.contact_us:
                Intent intent9 = new Intent(FoodDetailsActivity.this, ContactUs.class);
                startActivity(intent9);
                finish();
                Animatoo.animateFade(this);
                break;
        }
    }
}