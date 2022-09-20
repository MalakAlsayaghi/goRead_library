package com.goread.library.libraries.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.goread.library.R;
import com.goread.library.fragments.BookFragment;
import com.goread.library.fragments.HomeFragment;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class LibraryMainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference reference;
    String id, name, email, phone, user_type, token, password;
    FragmentManager fragmentManager;
    AnimatedBottomBar animatedBottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_main);

    }
}