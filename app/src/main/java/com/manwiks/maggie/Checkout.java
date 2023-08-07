package com.manwiks.maggie;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

public class Checkout extends AppCompatActivity implements View.OnClickListener{

    EditText edit_name, edit_email, edt_comment, edt_other_address, edt_phone;
    Spinner select_reg;
    TextView showVat, showCartTotal;
    Button submitOrder;
    CompositeDisposable compositeDisposable;
  GroceryShopAPI  mService;
    List<RegionsModel> regions;
    List<String> defaultRegionNames;
    private RegionsModel selectedRegions;
    String selectedRegionName, selectedDateTime;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView navigationBar;
    private RelativeLayout relativeLayout3Bookmarks, relativeLayout4Earnings;
    private TextView your_orders, favorite_orders, address, online_order_help, send_feedback, report_safety, rate_us, contact_us, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
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
                                submitOrder = findViewById(R.id.btn_place_order);
                                edit_name =  findViewById(R.id.edit_name);
                                edit_email = (EditText) findViewById(R.id.edit_email);
                                select_reg = (Spinner) findViewById(R.id.select_r);
                                edt_comment = (EditText) findViewById(R.id.edit_comment);
                                edt_other_address = (EditText) findViewById(R.id.edit_address);
                                edt_phone = (EditText) findViewById(R.id.edit_phone);
                                showVat = (TextView) findViewById(R.id.showVat);
                                showCartTotal = (TextView) findViewById(R.id.showCartTotal);
                                selectedRegionName = "";
                                selectedDateTime = "";

                                submitOrder.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        PlaceOrder();
                                    }
                                });

                                compositeDisposable.add(mService.getRegions("code", "default_name")
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(new Consumer<List<RegionsModel>>() {
                                            @Override
                                            public void accept(List<RegionsModel> regionsModelList) throws Exception {
                                                Checkout.this.regions = regionsModelList;

                                                defaultRegionNames = new ArrayList<>();
                                                for (RegionsModel region : regions) {
                                                    defaultRegionNames.add(region.getDefault_name());
                                                }

                                                ArrayAdapter<String> myAdapter = new ArrayAdapter<>(Checkout.this, android.R.layout.simple_spinner_item, defaultRegionNames);
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
                                                        selectedRegionName= (String) parent.getItemAtPosition(position);
                                                        for (RegionsModel region : regions) {
                                                            if (region.getDefault_name().equals(selectedRegionName)) {
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

                                Button pick_time = (Button) findViewById(R.id.pick_time);

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
                                                    Checkout.this,
                                                    new DatePickerDialog.OnDateSetListener() {
                                                        @Override
                                                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                                            mYear[0] = year;
                                                            mMonth[0] = month;
                                                            mDay[0] = day;

                                                            TimePickerDialog timePickerDialog = new TimePickerDialog(Checkout.this,
                                                                    new TimePickerDialog.OnTimeSetListener() {
                                                                        @Override
                                                                        public void onTimeSet(TimePicker timePicker, int hoursOfDay, int minutes) {
                                                                            mHour[0] = hoursOfDay;
                                                                            mMinute[0] = minutes;

                                                                            String formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", mYear[0], mMonth[0] + 1, mDay[0]);
                                                                            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", mHour[0], mMinute[0]);

                                                                            // Concatenate the date and time
                                                                            selectedDateTime = formattedDate + " " + formattedTime;
                                                                            TextView showTimeSet = (TextView) findViewById(R.id.showTimeSet);
                                                                            showTimeSet.setText(new StringBuilder("Set to be available at: ").append(selectedDateTime));
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

        onSetNavigationDrawerEvents();
    } // onCreate end tag
    public void PlaceOrder() {
        String orderComments = edt_comment.getText().toString();
        String userPhone = edt_phone.getText().toString();
        String userName = edit_name.getText().toString();
        String userEmail = edit_email.getText().toString().trim();
        String orderAddress = edt_other_address.getText().toString().trim();
        String selectRegions = selectedRegionName;
        String selectedTime = selectedDateTime;

        compositeDisposable.add(
                Common.cartRepository.getCartItems()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(carts -> {
                            boolean isAnyFieldEmpty = TextUtils.isEmpty(userName) || TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPhone) || TextUtils.isEmpty(orderAddress);
                            if (isAnyFieldEmpty) {
                                if (TextUtils.isEmpty(userName)) {
                                    edit_name.setError("Your name is required");
                                    return; // Don't proceed further, let the user fix the error
                                }
                                if (TextUtils.isEmpty(userEmail)) {
                                    edit_email.setError("Email is required");
                                    return;
                                }
                                if (TextUtils.isEmpty(userPhone)) {
                                    edt_phone.setError("Phone is required");
                                    return;
                                }
                                if (TextUtils.isEmpty(orderAddress)) {
                                    edt_other_address.setError("Order address is required");
                                    return;
                                }
                            } else {
                                sendOrderToServer(Common.cartRepository.sumPrice(), Common.cartRepository.sumVat(), Common.cartRepository.sumPricePlusTax(),
                                        carts, orderAddress, orderComments, userPhone, userName, userEmail, selectedTime, selectRegions);
                            }
                        }, throwable -> {
                        })
        );
    }
    private void sendOrderToServer(float sumPrice, float sumVat, float sumPricePlusTax, List<Cart> carts, String orderAddress, String orderComments, String userPhone, String userName, String userEmail, String selectedDateTime, String region) {
        if(carts.size()>0) {
            String OrderDetails = new Gson().toJson(carts);
            mService.submitOrder(sumPrice, sumVat, sumPricePlusTax, OrderDetails,orderAddress, orderComments,userPhone, userName, userEmail,selectedDateTime, region)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            Handler handler = new Handler();
                            ProgressDialog progressDialog = new ProgressDialog(Checkout.this);
                            progressDialog.setMessage("Please Wait...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            edit_email.setText("");
                            edit_name.setText("");
                            edt_phone.setText("");
                            edt_comment.setText("");
                            edt_other_address.setText("");
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Checkout.this);
                                    builder.setTitle("Order Success");
                                    View itemView = LayoutInflater.from(Checkout.this).inflate(R.layout.submit_message_layout, null);
                                    builder.setView(itemView);
                                    builder.setPositiveButton("GO HOME", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent myIntent = new Intent(Checkout.this, HomeActivity.class);
                                            myIntent.putExtra("startFragment", "orders");
                                            startActivity(myIntent);
                                            finish();
                                            Animatoo.animateSlideLeft(Checkout.this);
                                        }
                                    });
                                    Common.cartRepository.emptyCart();
                                    AlertDialog dialog = builder.create();
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.show();
                                }
                            }, 10000);
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("ERROR", Objects.requireNonNull(t.getMessage()));
                        }
                    });
        }
    }
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
                Intent intent1 = new Intent(Checkout.this, AboutUs.class);
                startActivity(intent1);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.relativeLayout4:
                Toast.makeText(this, "Earnings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.your_orders:
                Intent intent2 = new Intent(Checkout.this, CustomerOders.class);
                startActivity(intent2);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.favorite_orders:
                Intent intent3 = new Intent(Checkout.this, CustomerFavorites.class);
                startActivity(intent3);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.address:
                Intent intent4 = new Intent(Checkout.this, UserProfile.class);
                startActivity(intent4);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.online_order_help:
                Intent intent5 = new Intent(Checkout.this, OrderHelp.class);
                startActivity(intent5);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.send_feedback:
                Intent intent6 = new Intent(Checkout.this, CustomerFeedback.class);
                startActivity(intent6);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.report_safety:
                Intent intent7 = new Intent(Checkout.this, ReportEmergency.class);
                startActivity(intent7);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.rate_us:
                Intent intent8 = new Intent(Checkout.this, RateUs.class);
                startActivity(intent8);
                finish();
                Animatoo.animateFade(this);
                break;
            case R.id.contact_us:
                Intent intent9 = new Intent(Checkout.this, ContactUs.class);
                startActivity(intent9);
                finish();
                Animatoo.animateFade(this);
                break;
        }
    }
}