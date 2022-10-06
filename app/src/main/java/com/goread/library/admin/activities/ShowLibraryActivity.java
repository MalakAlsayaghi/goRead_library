package com.goread.library.admin.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.goread.library.R;
import com.goread.library.admin.fragments.LibraryBooksFragment;
import com.goread.library.admin.fragments.LibraryQuotesFragment;
import com.goread.library.models.User;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

public class ShowLibraryActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    LibraryBooksFragment libraryBooksFragment;
    LibraryQuotesFragment libraryQuotesFragment;
    SwitchButton switchButton;
    User library;
    DatabaseReference databaseReference;
    ImageView back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_library);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.view_pager);
        switchButton = findViewById(R.id.switchButton);
        back_btn = findViewById(R.id.btn_back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("Banned");
        library = (User) getIntent().getSerializableExtra("library");

        libraryBooksFragment = new LibraryBooksFragment();
        libraryQuotesFragment = new LibraryQuotesFragment();
        tabLayout.setupWithViewPager(viewPager);
        initTabLayout();
        getBanned();
    }

    private void getBanned() {
        databaseReference.child(library.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Boolean isDisabled = snapshot.getValue(Boolean.class);
                    if (isDisabled) {
                        switchButton.setChecked(false);
                    } else {
                        switchButton.setChecked(true);
                    }
                } else {
                    switchButton.setChecked(true);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    changeStatus(false);


                } else {

                    changeStatus(true);

                }


            }
        });
    }

    private void changeStatus(boolean b) {
        databaseReference.child(library.getId()).setValue(b);
    }

    public void initTabLayout() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);

        viewPagerAdapter.addFragment(libraryBooksFragment, "Books");
        viewPagerAdapter.addFragment(libraryQuotesFragment, "Quotes");
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_menu_book_24_maincolor);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_quotes);

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments = new ArrayList<>();
        List<String> fragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }
}