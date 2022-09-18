package com.goread.library.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.goread.library.R;
import com.goread.library.fragments.LibraryHomeFragment;

public class OnBordingActivity extends AppCompatActivity {
    Button done_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_bording);
        defineViews();

    }
    private void defineViews() {

        done_btn=findViewById(R.id.btn_done);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LibraryHomeFragment.class));
                finish();
            }
        });
}}