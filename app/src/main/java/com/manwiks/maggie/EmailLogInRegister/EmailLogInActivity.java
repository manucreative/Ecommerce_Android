package com.manwiks.maggie.EmailLogInRegister;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.manwiks.maggie.Fragments.OrdersFragment;
import com.manwiks.maggie.HomeActivity;
import com.manwiks.maggie.MainActivity;
import com.manwiks.maggie.OperationRetrofit.ApiClient;
import com.manwiks.maggie.OperationRetrofit.ApiInterface;
import com.manwiks.maggie.OperationRetrofit.Users;
import com.manwiks.maggie.R;
import com.manwiks.maggie.Sessions.SessionManager;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailLogInActivity extends AppCompatActivity {

    private EditText logMail, logPassword;
    private Button logBtn;
    private TextView textView;
    private ImageButton imageButton;
    String user_id;
    SessionManager sessionManager;

    public static ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_log_in);

        /////Hide status bar//////
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        sessionManager = new SessionManager(this);

        init();

        textView = (TextView) findViewById(R.id.txtSign_up);
        imageButton = (ImageButton)findViewById(R.id.imageButton);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmailLogInActivity.this, EmailRegisterActivity.class);
                startActivity(intent);
                Animatoo.animateInAndOut(EmailLogInActivity.this);
                finish();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmailLogInActivity.this, MainActivity.class);
                startActivity(intent);
                Animatoo.animateInAndOut(EmailLogInActivity.this);
                finish();
            }
        });
    }

    private void init() {
        logMail = (EditText)findViewById(R.id.logMail);
        logPassword = (EditText)findViewById(R.id.logPassword);
        logBtn = (Button)findViewById(R.id.btnLogin);

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Login();
            }
        });
    }

    private void Login() {
        String user_email = logMail.getText().toString().trim();
        String user_password = logPassword.getText().toString().trim();

        if(TextUtils.isEmpty(user_email)){
            logMail.setError("Please enter your email");
        }else if(TextUtils.isEmpty(user_password)) {
            logPassword.setError("Password is Required");
        }
        else
        {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Logging...");
            dialog.setMessage("Please wait while we are checking your credentials");
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);

            Call<Users> call = apiInterface.performEmailLogin(user_email,user_password);
            call.enqueue(new Callback<Users>() {
                @Override
                public void onResponse(Call<Users> call, Response<Users> response) {
                    if (response.body().getResponse().equals("ok"))
                    {
                        user_id = response.body().getUserId();
                        sessionManager.createSession(user_id);
                        Intent intent = new Intent(EmailLogInActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        Animatoo.animateInAndOut(EmailLogInActivity.this);
                        dialog.dismiss();
                    }
                    else if (response.body().getResponse().equals("not_registered"))
                    {
                        Toast.makeText(EmailLogInActivity.this, "you are not yet registered please register first", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<Users> call, Throwable t) {
                    Toast.makeText(EmailLogInActivity.this, "Something went wrong, please try again latter ", Toast.LENGTH_SHORT).show();
                 dialog.dismiss();
                }
            });
        }
    }
}