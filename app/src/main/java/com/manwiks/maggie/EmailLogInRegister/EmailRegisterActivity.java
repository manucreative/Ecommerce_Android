package com.manwiks.maggie.EmailLogInRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.manwiks.maggie.Fragments.OrdersFragment;
import com.manwiks.maggie.HomeActivity;
import com.manwiks.maggie.MainActivity;
import com.manwiks.maggie.OperationRetrofit.ApiClient;
import com.manwiks.maggie.OperationRetrofit.ApiInterface;
import com.manwiks.maggie.OperationRetrofit.Users;
import com.manwiks.maggie.R;
import com.manwiks.maggie.Sessions.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailRegisterActivity extends AppCompatActivity {
   private EditText name, email, password;
   private Button regBtn;
    private TextView textView;
    private ImageButton imageButton2;

    public static ApiInterface apiInterface;
    String user_id;

    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_register);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        sessionManager = new SessionManager(this);

        init();

        textView = (TextView)findViewById(R.id.txtSign_in);
        imageButton2 = (ImageButton)findViewById(R.id.imageButton2);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmailRegisterActivity.this, EmailLogInActivity.class);
                startActivity(intent);
                Animatoo.animateInAndOut(EmailRegisterActivity.this);
                finish();
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmailRegisterActivity.this, MainActivity.class);
                startActivity(intent);
                Animatoo.animateInAndOut(EmailRegisterActivity.this);
                finish();
            }
        });
    }

    private void init() {
        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        regBtn = (Button) findViewById(R.id.button2);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registration();
            }
        });
    }

    private void Registration() {
        String user_name = name.getText().toString().trim();
        String user_email = email.getText().toString().trim();
        String user_password = password.getText().toString().trim();

        if(TextUtils.isEmpty(user_name)){
            name.setError("Name is Required");
        }else if(TextUtils.isEmpty(user_email)) {
            email.setError("Email is Required");
        }else if(TextUtils.isEmpty(user_password)) {
            password.setError("Password is Required");
        }
        else
        {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Submitting...");
            dialog.setMessage("Please wait while we are submitting your data");
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);

            Call<Users> call = apiInterface.performEmailRegistration(user_name, user_email, user_password);
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(Call<Users> call, Response<Users> response) {
                    if(response.body().getResponse().equals("ok"))
                    {
                        user_id = response.body().getUserId();
                        sessionManager.createSession(user_id);
                        Intent intent = new Intent(EmailRegisterActivity.this, HomeActivity.class);
                        intent.putExtra("startFragment", "orders");
                        startActivity(intent);
                        finish();
                        Animatoo.animateInAndOut(EmailRegisterActivity.this);
                        Toast.makeText(EmailRegisterActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    else if(response.body().getResponse().equals("failed"))
                    {
                        Toast.makeText(EmailRegisterActivity.this, "Operation Fail to work Please try again", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    else if(response.body().getResponse().equals("already"))
                    {
                        Toast.makeText(EmailRegisterActivity.this, "This details exist in the database please Log in", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<Users> call, Throwable t) {
                    Toast.makeText(EmailRegisterActivity.this, "Something went wrong, please try again latter ", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
    }
}