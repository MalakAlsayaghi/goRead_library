package com.goread.library.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.goread.library.R;

public class AddDriverActivity extends AppCompatActivity {
    ImageView back_btn;
    Button addd_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);
        defineViews();

    }
    private void defineViews() {
        back_btn=findViewById(R.id.btn_back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AllAdminsActivity.class));
                finish();
            }
        });

        addd_btn=findViewById(R.id.btn_addd);
        addd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(),.class));
                finish();
            }
        });
}}