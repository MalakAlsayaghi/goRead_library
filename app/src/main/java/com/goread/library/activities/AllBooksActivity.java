package com.goread.library.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.goread.library.R;
import com.goread.library.fragments.LibraryHomeFragment;

public class AllBooksActivity extends AppCompatActivity {
    ImageView back_btn;
    ImageView add_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_books);
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

        add_btn=findViewById(R.id.btn_add);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddAdminActivity.class));
                finish();
            }
        });
}
}