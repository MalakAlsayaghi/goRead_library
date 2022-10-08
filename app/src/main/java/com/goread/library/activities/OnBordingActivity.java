package com.goread.library.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.goread.library.R;
import com.goread.library.base.BaseActivity;
import com.goread.library.libraries.activities.LibraryMainActivity;

public class OnBordingActivity extends BaseActivity {
    Button done_btn;
    CheckBox cb_accept;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
         databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("Library");
        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("new").setValue(false);


        defineViews();

    }

    @Override
    public int defineLayout() {
        return R.layout.activity_on_bording;
    }

    private void defineViews() {
//if check cb_accept open the home
        done_btn = findViewById(R.id.btn_done);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LibraryMainActivity.class));
                finish();
            }
        });
        cb_accept = findViewById(R.id.cb_accept);
        cb_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                done_btn.setEnabled(true);
            }
        });
    }
}