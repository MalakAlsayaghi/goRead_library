package com.goread.library.libraries.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.goread.library.R;
import com.goread.library.base.BaseActivity;
import com.goread.library.fragments.LibraryHomeFragment;

public class LibraryProfileActivity extends BaseActivity {
    ImageView back_btn;
    ImageButton edit_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineViews();

    }

    @Override
    public int defineLayout() {
        return R.layout.activity_library_profile;
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
        edit_pic=findViewById(R.id.edit_pic);
        edit_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}