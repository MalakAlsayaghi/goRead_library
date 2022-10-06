package com.goread.library.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.goread.library.R;
import com.goread.library.auth.LoginActivity;
import com.goread.library.base.BaseActivity;
import com.goread.library.base.LanguageManager;
import com.goread.library.libraries.activities.LibraryMainActivity;

public class SplashActivity extends BaseActivity {

    FirebaseAuth mAuth;
    Animation anim;
    ImageView imageView;
    LanguageManager languageManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        languageManager = new LanguageManager(this);
        languageManager.updateResources("ar");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 2000);
        imageView = findViewById(R.id.imageView); // Declare an imageView to show the animation.
        ////////////////////
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in); // Create the animation.
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        imageView.startAnimation(anim);
        /////////////////////
    }

    @Override
    public int defineLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


}