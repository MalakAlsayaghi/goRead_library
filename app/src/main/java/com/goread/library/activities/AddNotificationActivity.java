package com.goread.library.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.goread.library.R;
import com.goread.library.fragments.LibraryHomeFragment;

public class AddNotificationActivity extends AppCompatActivity {
    ImageView back_btn;
    Button publish_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);
        defineViews();

    }
    private void defineViews() {
        back_btn=findViewById(R.id.btn_back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminMainActivity.class));
                finish();
            }
        });

        publish_btn=findViewById(R.id.btn_publish);
        publish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(),.class));
                finish();
            }
        });
}}