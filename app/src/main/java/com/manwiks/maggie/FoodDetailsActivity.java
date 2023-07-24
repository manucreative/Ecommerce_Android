package com.manwiks.maggie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.manwiks.maggie.Adapters.TopUpsAdapter;
import com.manwiks.maggie.Database.ModelDB.Cart;
import com.manwiks.maggie.Models.TopUpsModel;
import com.manwiks.maggie.RetroTwo.Common;
import com.manwiks.maggie.Retrofit.GroceryShopAPI;
import com.manwiks.maggie.Sessions.SessionManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FoodDetailsActivity extends AppCompatActivity {

    ImageView image_food;
    TextView food_name, food_price, food_description, region_test;
    ElegantNumberButton number_button;
    FloatingActionButton btnCart;
    Button addCart;

    GroceryShopAPI mService;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        sessionManager = new SessionManager(this);
        mService = Common.getAPI();

        image_food = (ImageView) findViewById(R.id.image_food);
        food_name = (TextView) findViewById(R.id.food_name);
        food_price = (TextView) findViewById(R.id.food_price);
        food_description = (TextView) findViewById(R.id.food_description);
        number_button = (ElegantNumberButton) findViewById(R.id.number_button);
        btnCart = (FloatingActionButton) findViewById(R.id.btnCart);
        addCart = (Button) findViewById(R.id.addCart);


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
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddToCartDialog(number_button.getNumber());
            }
        });

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


}