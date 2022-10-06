package com.goread.library;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.goread.library.base.BaseActivity;

public class ResetPasswordDialog extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int defineLayout() {
        return R.layout.reset_password_dialog;
    }
}