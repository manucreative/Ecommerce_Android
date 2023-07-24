package com.manwiks.maggie.PhoneLoginRegistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.manwiks.maggie.HomeActivity;
import com.manwiks.maggie.MainActivity;
import com.manwiks.maggie.OperationRetrofit.ApiClient;
import com.manwiks.maggie.OperationRetrofit.ApiInterface;
import com.manwiks.maggie.OperationRetrofit.Users;
import com.manwiks.maggie.R;
import com.manwiks.maggie.Sessions.SessionManager;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneLoginActivity extends AppCompatActivity {

    private EditText phone,otp;
    private Button logBtn,btnOtp;
    private TextView phoneSignIn;
    private ImageButton imageButton3;
    String user_id;


    ////////////Phone opt/////////////
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private FirebaseAuth mAuth;
    //progress dialog
    ImageView dialog;
    SessionManager sessionManager;

    public static ApiInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        /////Hide status bar//////
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        mAuth = FirebaseAuth.getInstance();

        sessionManager = new SessionManager(this);
        init();

        phoneSignIn = (TextView)findViewById(R.id.phone_sign_in);
        imageButton3 = (ImageButton)findViewById(R.id.imageButton3);

        phoneSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneLoginActivity.this, PhoneRegisterActivity.class);
                startActivity(intent);
                Animatoo.animateInAndOut(PhoneLoginActivity.this);
                finish();
            }
        });
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneLoginActivity.this, MainActivity.class);
                startActivity(intent);
                Animatoo.animateInAndOut(PhoneLoginActivity.this);
                finish();
            }
        });
    }

    private void init() {
        phone = (EditText) findViewById(R.id.phone);
        otp = (EditText) findViewById(R.id.otp);
        logBtn = (Button) findViewById(R.id.button2);
        btnOtp = (Button) findViewById(R.id.button3);

        //progress dialog
        dialog = (ImageView)findViewById(R.id.imageView3);
        Glide.with(this).load(R.drawable.loader).into(dialog);
        //////////////////////////////////////////////

        ////////////////////phone otp callbacks//////////////
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
            {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e)
            {
                dialog.setVisibility(View.GONE);
                otp.setVisibility(View.GONE);
                phone.setVisibility(View.VISIBLE);
                btnOtp.setVisibility(View.GONE);
                logBtn.setVisibility(View.VISIBLE);
                Toast.makeText(PhoneLoginActivity.this, "Invalid Phone number"+e.getMessage(), Toast.LENGTH_LONG).show();
            }
            public void onCodeSend(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token)
            {
                //save verification ID and resend token so than we can use then latter
                mVerificationId = verificationId;
                mResendToken = token;


                Toast.makeText(PhoneLoginActivity.this, "Code has been sent, please check and verify", Toast.LENGTH_LONG).show();

                otp.setVisibility(View.VISIBLE);
                phone.setVisibility(View.GONE);
                btnOtp.setVisibility(View.VISIBLE);
                logBtn.setVisibility(View.GONE);
                dialog.setVisibility(View.GONE);
            }
        };

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_phone = phone.getText().toString().trim();
                if (TextUtils.isEmpty(user_phone)) {
                    phone.setError("Phone is needed");
                }
                if (user_phone.length() != 9) {
                    phone.setError("number not valid please check");
                }
                else {
                    dialog.setVisibility(View.VISIBLE);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+254"+ phone.getText().toString().trim(),   //Phone number to verify
                            60,   //Time out duration
                            TimeUnit.SECONDS, //unit of time
                            PhoneLoginActivity.this,
                            callbacks);
                }
            }
        });

        btnOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp.getText().toString().equals(""))
                {
                    Toast.makeText(PhoneLoginActivity.this, "Please enter the 6 digs sent to your number", Toast.LENGTH_SHORT).show();
                }
                if(otp.getText().toString().length() !=6)
                {
                    Toast.makeText(PhoneLoginActivity.this, "Your should give the correct 6 digits", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    dialog.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp.getText().toString().trim());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Login();
                        }
                        else
                        {
                            dialog.setVisibility(View.GONE);

                        }
                    }
                });
    }
    private void Login() {


        Call<Users> call = apiInterface.performPhoneLoin(phone.getText().toString());
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {

                if(response.body().getResponse().equals("ok"))
                {
                    user_id = response.body().getUserId();
                    sessionManager.createSession(user_id);
                    Intent intent = new Intent(PhoneLoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    Animatoo.animateInAndOut(PhoneLoginActivity.this);

                    dialog.setVisibility(View.GONE);
                }
                else if(response.body().getResponse().equals("not_registered"))
                {
                    Toast.makeText(PhoneLoginActivity.this, "You are not yet registered please register first", Toast.LENGTH_SHORT).show();
                    dialog.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {

                Toast.makeText(PhoneLoginActivity.this, "Something went wrong, please try again latter ", Toast.LENGTH_SHORT).show();
                dialog.setVisibility(View.GONE);
            }
        });
    }
}