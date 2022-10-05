package com.goread.library.admin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.goread.library.R;

public class AddAdminActivity extends AppCompatActivity {
    ImageView back_btn;
    Button add_btn;
    EditText Fname_et, Lname_et, userName_et, password_et, email_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        defineViews();

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    private void defineViews() {
        back_btn = findViewById(R.id.btn_back);
        add_btn = findViewById(R.id.btn_addd);
        Fname_et = findViewById(R.id.et_Fname);
        Lname_et = findViewById(R.id.et_Lname);
        password_et = findViewById(R.id.et_password);
        email_et = findViewById(R.id.et_email);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AllAdminsActivity.class));
                finish();
            }
        });

    }
}