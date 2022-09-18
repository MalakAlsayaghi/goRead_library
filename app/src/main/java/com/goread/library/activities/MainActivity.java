package com.goread.library.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.goread.library.R;
import com.goread.library.fragments.BookFragment;
import com.goread.library.fragments.HomeFragment;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference reference;
    String id, name, email, phone, user_type, token, password;
    FragmentManager fragmentManager;
    AnimatedBottomBar animatedBottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        reference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        initViews(savedInstanceState);




    }
    @SuppressLint("NonConstantResourceId")
    private void initViews(Bundle savedInstanceState) {
        /**
         * Menu Bottom Navigation Drawer
         * */
        animatedBottomBar = findViewById(R.id.navigation);

        if (savedInstanceState == null) {
            animatedBottomBar.selectTabById(R.id.nav_home, true);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.container, homeFragment)
                    .commit();
        }


        animatedBottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {
                Fragment fragment = null;

                switch (tab.getId()) {
                    case R.id.nav_home:
                        fragment = new BookFragment();
                    //    fragment = new HomeFragment();
                        //  setActionBarTitle(getString(R.string.home));
                        break;
                    case R.id.nav_all:

                        fragment = new HomeFragment();
                        //   setActionBarTitle(getString(R.string.favorite));
                        break;



                }

                if (fragment != null) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment)
                            .commit();
                } else {
                    //  Log.e(TAG, "Error in creating Fragment");
                }
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });



        /**
         * Menu Navigation Drawer
         **/

    }

}