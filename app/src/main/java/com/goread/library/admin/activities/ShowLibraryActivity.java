package com.goread.library.admin.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.goread.library.R;
import com.goread.library.admin.fragments.LibraryBooksFragment;
import com.goread.library.admin.fragments.LibraryQuotesFragment;

import java.util.ArrayList;
import java.util.List;

public class ShowLibraryActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    LibraryBooksFragment libraryBooksFragment;
    LibraryQuotesFragment libraryQuotesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_library);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.view_pager);

        libraryBooksFragment = new LibraryBooksFragment();
        libraryQuotesFragment = new LibraryQuotesFragment();
        tabLayout.setupWithViewPager(viewPager);
        initTabLayout();
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