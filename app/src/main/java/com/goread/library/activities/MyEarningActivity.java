package com.goread.library.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.goread.library.R;

public class MyEarningActivity extends AppCompatActivity {
    ImageView back_btn;
    RecyclerView allMyEarnings_recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_earning);
        defineViews();

    }
    private void defineViews() {
        back_btn=findViewById(R.id.btn_back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AdminMainActivity.class));
                finish();
            }
        });
        allMyEarnings_recyclerView= allMyEarnings_recyclerView.findViewById(R.id.recycler_MyEarnings);
    }
}