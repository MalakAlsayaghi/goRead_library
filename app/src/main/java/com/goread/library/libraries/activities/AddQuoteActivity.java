package com.goread.library.libraries.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.goread.library.R;
import com.goread.library.fragments.LibraryHomeFragment;

public class AddQuoteActivity extends AppCompatActivity {
    ImageView back_btn;
    Button addd_btn;
    EditText et_quote,et_quote_bookName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quote);
        defineViews();

    }
    private void defineViews() {
        back_btn=findViewById(R.id.btn_back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LibraryHomeFragment.class));
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
        et_quote=findViewById(R.id.et_quote);
        et_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        et_quote_bookName=findViewById(R.id.et_quote_bookName);
        et_quote_bookName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
}}