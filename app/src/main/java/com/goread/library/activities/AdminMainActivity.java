package com.goread.library.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.card.MaterialCardView;
import com.goread.library.R;

public class AdminMainActivity extends AppCompatActivity implements View.OnClickListener {
    MaterialCardView cvLibraries;
    MaterialCardView cvDrivers;
    MaterialCardView cvEarnings;
    MaterialCardView cvOrders;
    MaterialCardView cvUsers;
    MaterialCardView cvNotifications;
    MaterialCardView cvCommunity;
    MaterialCardView cvAdmins;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        defineViews();
    }

    private void defineViews() {
        cvLibraries = findViewById(R.id.card_libraries);
        cvLibraries.setOnClickListener(this);

        cvDrivers = findViewById(R.id.card_drivers);
        cvDrivers.setOnClickListener(this);

        cvEarnings = findViewById(R.id.card_earnings);
        cvEarnings.setOnClickListener(this);

        cvOrders = findViewById(R.id.card_orders);
        cvOrders.setOnClickListener(this);

        cvUsers = findViewById(R.id.card_users);
        cvUsers.setOnClickListener(this);

        cvNotifications = findViewById(R.id.card_notifications);
        cvNotifications.setOnClickListener(this);

        cvCommunity = findViewById(R.id.card_community);
        cvCommunity.setOnClickListener(this);

        cvAdmins = findViewById(R.id.card_admin);
        cvAdmins.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_libraries:
                startActivity(new Intent(getApplicationContext(),AllLibrariesActivity.class));
                break;

            case R.id.card_drivers:
                startActivity(new Intent(getApplicationContext(),AllDriversActivity.class));
                break;

            case R.id.card_earnings:
                startActivity(new Intent(getApplicationContext(),MyEarningActivity.class));
                break;

            case R.id.card_orders:
                startActivity(new Intent(getApplicationContext(),AllOrdersActivity.class));
                break;

            case R.id.card_users:
                startActivity(new Intent(getApplicationContext(),AllUsersActivity.class));
                break;

            case R.id.card_notifications:
                startActivity(new Intent(getApplicationContext(),AllNotificationActivity.class));
                break;


/*
                it should open the community home
*/
            /*case R.id.card_community:
                startActivity(new Intent(getApplicationContext(),.class));
                break;
*/
            case R.id.card_admin:_:
                startActivity(new Intent(getApplicationContext(), AllAdminsActivity.class));
                break;

            default:
                setContentView(R.layout.activity_admin_main);
                break;
        }

    }
}