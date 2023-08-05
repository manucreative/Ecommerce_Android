package com.manwiks.maggie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.manwiks.maggie.Adapters.PlateAdapter;
import com.manwiks.maggie.EmailLogInRegister.EmailLogInActivity;
import com.manwiks.maggie.Fragments.OrdersFragment;
import com.manwiks.maggie.Models.CategoryModel;
import com.manwiks.maggie.OperationRetrofit.ApiClient;
import com.manwiks.maggie.OperationRetrofit.ApiInterface;
import com.manwiks.maggie.OperationRetrofit.Users;
import com.manwiks.maggie.PhoneLoginRegistration.PhoneLoginActivity;
import com.manwiks.maggie.Sessions.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<CategoryModel> categoryModelList;
    private PlateAdapter plateAdapter;
    private LinearLayout emailContinue;
    private LinearLayout phoneContinue;

    public static ApiInterface apiInterface;

//    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        sessionManager = new SessionManager(this);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);


        phoneContinue = (LinearLayout)findViewById(R.id.linear2);
        emailContinue = (LinearLayout)findViewById(R.id.linear1);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setKeepScreenOn(true);
        recyclerView.setHasFixedSize(true);

       categoryModelList = new ArrayList<>();
        Call<Users> categoryCall = apiInterface.getCategory();
        categoryCall.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {

                categoryModelList = response.body().getCategory();

                plateAdapter = new PlateAdapter(categoryModelList,getApplicationContext());
                recyclerView.setAdapter(plateAdapter);
                plateAdapter.notifyDataSetChanged();
                autoScroll();
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {

            }
        });

        //Email LogIn
        emailContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EmailLogInActivity.class);
                startActivity(intent);
                Animatoo.animateInAndOut(MainActivity.this);
            }
        });

        phoneContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhoneLoginActivity.class);
                startActivity(intent);
                Animatoo.animateInAndOut(MainActivity.this);
            }
        });
    }
    public  void autoScroll(){
        final  int speedScroll = 3;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;
            @Override
            public void run() {
                if(count == plateAdapter.getItemCount())
                    count=0;
                if(count< plateAdapter.getItemCount()){
                    recyclerView.smoothScrollToPosition(++count);
                    handler.postDelayed(this,speedScroll);
                }
            }
        };
      handler.postDelayed(runnable,speedScroll);
    }

    public void goToHomePage(View view) {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
        Animatoo.animateInAndOut(MainActivity.this);

    }

    @Override
    protected void onStart() {
        super.onStart();
//        if(sessionManager.isLogin())
//        {
//            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//            startActivity(intent);
//            finish();
//            Animatoo.animateInAndOut(MainActivity.this);
//        }
//        else
//        {
//
//        }
    }
}