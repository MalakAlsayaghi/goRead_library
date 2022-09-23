package com.goread.library.libraries.activities;

import android.os.Bundle;
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
import com.goread.library.libraries.fragment.NewOrdersFragment;
import com.goread.library.libraries.fragment.OldOrdersFragment;

import java.util.ArrayList;
import java.util.List;

public class LibraryOrdersActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_library_orders);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.view_pager);

        newOrdersFragment = new NewOrdersFragment();
        oldOrdersFragment = new OldOrdersFragment();
       tabLayout.setupWithViewPager(viewPager);
       initTabLayout();


    }


    public void initTabLayout() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);

        viewPagerAdapter.addFragment(newOrdersFragment, "New Orders");
        viewPagerAdapter.addFragment(oldOrdersFragment, "Old  Orders");
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