package com.goread.library.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.goread.library.R;
import com.goread.library.auth.LoginActivity;

public class SplashActivity  extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

              if(mAuth.getCurrentUser()!= null){
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
                else {
                  startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                  finish();
            };


            }
        }, 3000);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


}