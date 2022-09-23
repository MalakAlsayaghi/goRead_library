package com.goread.library.admin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.goread.library.R;

public class AddNotificationActivity extends AppCompatActivity {
    ImageView back_btn;
    Button publish_btn;
    EditText et_notification_title, et_notification_body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);

    }
}