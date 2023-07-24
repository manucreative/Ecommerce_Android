package com.manwiks.maggie;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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

import com.google.gson.Gson;
import com.manwiks.maggie.Adapters.ICartAdapter;
import com.manwiks.maggie.Database.ModelDB.Cart;
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

        btn_plce_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceOrder();
            }
        });

        LoadCartItems();
    }

//    private void loadRegions() {
//        compositeDisposable.add(mService.getRegions("code", "default_name")
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(this::onLoadRegionsSuccess, this::onLoadRegionsFailed));
//    }
//
//    private void onLoadRegionsSuccess(List<RegionsModel> regions) {
//        this.regions = regions;
//
//        defaultRegionNames = new ArrayList<>();
//        for (RegionsModel region : regions) {
//            defaultRegionNames.add(region.getDefault_name());
//        }
//
//
//        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, defaultRegionNames);
//        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        select_region.setAdapter(myAdapter);
//        // Set the default region
//        if (regions != null && !regions.isEmpty()) {
//            select_region.setSelection(0);
//            selectedRegions = regions.get(0);
//        }
//
//        select_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selectedRegionName = (String) parent.getItemAtPosition(position);
//                for (RegionsModel region : regions) {
//                    if (region.getDefault_name().equals(selectedRegionName)) {
//                        selectedRegions = region;
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // Do nothing
//            }
//        });
//    }
//
//    private void onLoadRegionsFailed(Throwable throwable) {
//        // Handle the error
//    }



    private void PlaceOrder() {
        // Show the cart dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Submit Cart");

        View submit_order_layout = LayoutInflater.from(this).inflate(R.layout.submit_cart_layout,null);

        EditText edit_name = (EditText)submit_order_layout.findViewById(R.id.edit_name);
        EditText edit_email = (EditText)submit_order_layout.findViewById(R.id.edit_email);
        Spinner select_reg = (Spinner)submit_order_layout.findViewById(R.id.select_r);
        EditText edt_comment = (EditText)submit_order_layout.findViewById(R.id.edit_comment);
        EditText edt_other_address = (EditText)submit_order_layout.findViewById(R.id.edit_address);
        EditText edt_phone = (EditText)submit_order_layout.findViewById(R.id.edit_phone);
        TextView showVat = (TextView)submit_order_layout.findViewById(R.id.showVat);
        TextView showCartTotal = (TextView) submit_order_layout.findViewById(R.id.showCartTotal);
        final String[] selectedRegionName = new String[1];
        final String[] selectedDateTime = new String[1];

        compositeDisposable.add(mService.getRegions("code", "default_name")
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Consumer<List<RegionsModel>>() {
            @Override
            public void accept(List<RegionsModel> regionsModelList) throws Exception {
                CartActivity.this.regions = regionsModelList;

                defaultRegionNames = new ArrayList<>();
                for (RegionsModel region : regions) {
                    defaultRegionNames.add(region.getDefault_name());
                }

                ArrayAdapter<String> myAdapter = new ArrayAdapter<>(CartActivity.this, android.R.layout.simple_spinner_item, defaultRegionNames);
                myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_reg.setAdapter(myAdapter);
                // Set the default region
                if (regions != null && !regions.isEmpty()) {
                    select_reg.setSelection(0);
                    selectedRegions = regions.get(0);
                 }
                    select_reg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedRegionName[0] = (String) parent.getItemAtPosition(position);
                            for (RegionsModel region : regions) {
                                if (region.getDefault_name().equals(selectedRegionName[0])) {
                            selectedRegions = region;
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        }
                        }));


// handle the selection ans set to a variable

        Button pick_time = (Button)submit_order_layout.findViewById(R.id.pick_time);

        pick_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                final int[] mYear = new int[1];
                final int[] mMonth = new int[1];
                final int[] mDay = new int[1];
                final int[] mHour = new int[1];
                final int[] mMinute = new int[1];
                mYear[0] = calendar.get(Calendar.YEAR);
                mMonth[0] = calendar.get(Calendar.MONTH);
                mDay[0] = calendar.get(Calendar.DAY_OF_MONTH);
                mHour[0] = calendar.get(Calendar.HOUR_OF_DAY);
                mMinute[0] = calendar.get(Calendar.MINUTE);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            CartActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                    mYear[0] = year;
                                    mMonth[0] = month;
                                    mDay[0] = day;

                                    TimePickerDialog timePickerDialog = new TimePickerDialog(CartActivity.this,
                                            new TimePickerDialog.OnTimeSetListener(){
                                                @Override
                                                public void onTimeSet(TimePicker timePicker, int hoursOfDay, int minutes) {
                                                    mHour[0] = hoursOfDay;
                                                    mMinute[0] = minutes;

                                                    String formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", mYear[0], mMonth[0]+1, mDay[0]);
                                                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", mHour[0], mMinute[0]);

                                                    // Concatenate the date and time
                                                    selectedDateTime[0] = formattedDate + " " + formattedTime;
                                                    TextView showTimeSet = (TextView)submit_order_layout.findViewById(R.id.showTimeSet);
                                                    showTimeSet.setText(new StringBuilder("Set to be available at: ").append(selectedDateTime[0]));
                                                }
                                }, hour, minute, false);
                                    timePickerDialog.show();
                            }

                        }, mYear[0], mMonth[0], mDay[0]);
                    datePickerDialog.show();
                }
            }
        });

        showVat.setText(new StringBuilder("Tax/VAT: ").append(Common.cartRepository.sumVat()));
        showCartTotal.setText(new StringBuilder("Total Bill: ").append(Common.cartRepository.sumPricePlusTax()));

        //event
//        rdi_user_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked)
//                    edt_other_address.setEnabled(false);
//            }
//        });


          builder.setView(submit_order_layout);
          builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  dialog.dismiss();
              }
          });
          builder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  String orderComments = edt_comment.getText().toString();
                  String userPhone = edt_phone.getText().toString();
                  String userName = edit_name.getText().toString();
                  String userEmail = edit_email.getText().toString();
                  String orderAddress = edt_other_address.getText().toString();

                  String selectRegions = selectedRegionName[0];
                  String selectedTime = selectedDateTime[0];


                 compositeDisposable.add(
                         Common.cartRepository.getCartItems()
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribeOn(Schedulers.io())
                         .subscribe(new Consumer<List<Cart>>() {
                             @Override
                             public void accept(List<Cart> carts) {
                                 if(TextUtils.isEmpty(userName)){
                                     Toast.makeText(CartActivity.this, "Your name is Required", Toast.LENGTH_LONG).show();
                                 } else if (TextUtils.isEmpty(userEmail)) {
                                     Toast.makeText(CartActivity.this, "Email is Required", Toast.LENGTH_LONG).show();
                                 } else if (TextUtils.isEmpty(userPhone)) {
                                     Toast.makeText(CartActivity.this, "Phone is required ", Toast.LENGTH_LONG).show();
                                 } else if (TextUtils.isEmpty(orderAddress)) {
                                     Toast.makeText(CartActivity.this, "Order address is required", Toast.LENGTH_LONG).show();
                                 } else {
//                                      selectRegions
                                     sendOrderToServer(Common.cartRepository.sumPrice(), Common.cartRepository.sumVat(), Common.cartRepository.sumPricePlusTax(),
                                             carts, orderAddress, orderComments, userPhone, userName, userEmail, selectedTime, selectRegions);
                                 }
                             }
                         })
                 );
              }
          });
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
    }

    private void sendOrderToServer(float sumPrice, float sumVat, float sumPricePlusTax, List<Cart> carts, String orderAddress, String orderComments, String userPhone, String userName, String userEmail, String selectedDateTime, String region) {

        if(carts.size()>0) {
            String OrderDetails = new Gson().toJson(carts);

            mService.submitOrder(sumPrice, sumVat, sumPricePlusTax, OrderDetails,orderAddress, orderComments,userPhone, userName, userEmail,selectedDateTime, region)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Toast.makeText(CartActivity.this, "Order Submitted Successful", Toast.LENGTH_SHORT).show();

                            //clear cart
                            Common.cartRepository.emptyCart();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("ERROR", Objects.requireNonNull(t.getMessage()));
                        }
                    });
        }
    }

    private void SubmitOrder() {

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