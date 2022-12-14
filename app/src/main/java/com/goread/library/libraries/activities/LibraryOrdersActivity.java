package com.goread.library.libraries.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.goread.library.R;
import com.goread.library.admin.activities.AdminMainActivity;
import com.goread.library.base.BaseActivity;
import com.goread.library.libraries.fragment.NewOrdersFragment;
import com.goread.library.libraries.fragment.OldOrdersFragment;

import java.util.ArrayList;
import java.util.List;

public class LibraryOrdersActivity extends BaseActivity {
    ImageView back_btn;
    RecyclerView allLibrariesOrders_recyclerView;
    ViewPager viewPager;
    TabLayout tabLayout;
    NewOrdersFragment newOrdersFragment;
    OldOrdersFragment oldOrdersFragment;
    String library_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.view_pager);
        back_btn = findViewById(R.id.btn_back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        newOrdersFragment = new NewOrdersFragment();
        oldOrdersFragment = new OldOrdersFragment();
       tabLayout.setupWithViewPager(viewPager);
       initTabLayout();


    }

    @Override
    public int defineLayout() {
        return R.layout.activity_library_orders;
    }


    public void initTabLayout() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);

        viewPagerAdapter.addFragment(newOrdersFragment, getString(R.string.new_order_lib));
        viewPagerAdapter.addFragment(oldOrdersFragment, getString(R.string.old_order_lib));
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_new_order_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_order_icon);

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