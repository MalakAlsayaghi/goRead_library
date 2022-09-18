package com.goread.library.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.card.MaterialCardView;
import com.goread.library.R;

public class AdminMainActivity extends AppCompatActivity implements View.OnClickListener {
    MaterialCardView cvLibraries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        defineViews();
    }

    private void defineViews() {
        cvLibraries = findViewById(R.id.card_libraries);
        cvLibraries.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_libraries:
                startActivity(new Intent(getApplicationContext(),AllLibrariesActivity.class));
                break;


        }

    }
}