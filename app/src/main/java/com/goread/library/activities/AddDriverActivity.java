package com.goread.library.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.goread.library.R;

public class AddDriverActivity extends AppCompatActivity {
    ImageView back_btn;
    Button addd_btn;
    EditText et_driver_name ,et_driver_phone;
    ImageButton edit_pic;
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

        et_driver_name=findViewById(R.id.et_driver_name);
        et_driver_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        et_driver_phone=findViewById(R.id.et_driver_phone);
        et_driver_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        edit_pic=findViewById(R.id.edit_pic);
        edit_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

}}