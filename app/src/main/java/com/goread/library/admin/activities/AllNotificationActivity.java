package com.goread.library.admin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.goread.library.R;

public class AllNotificationActivity extends AppCompatActivity {
    ImageView back_btn;
    ImageView add_btn;
    RecyclerView allNotifications_recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notification);
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
        add_btn=findViewById(R.id.btn_add);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddAdminActivity.class));
                finish();
            }
        });
        allNotifications_recyclerView= allNotifications_recyclerView.findViewById(R.id.recycler_allNotifications);

    }
}