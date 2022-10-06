package com.goread.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.goread.library.admin.activities.AllLibrariesActivity;
import com.goread.library.base.BaseActivity;

public class ProfileAdminActivity extends BaseActivity {
    ImageView back_btn;

    ImageButton edit_pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineViews();

    }

    @Override
    public int defineLayout() {
        return R.layout.activity_profile_admin;
    }

    private void defineViews() {

        edit_pic=findViewById(R.id.edit_pic);
        edit_pic.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        }
    });
    back_btn=findViewById(R.id.btn_back);
        back_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), AllLibrariesActivity.class));
            finish();
        }
    });
    }
}