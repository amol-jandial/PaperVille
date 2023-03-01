package com.telelab.paperville;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.telelab.paperville.fragments.BookmarkFragment;
import com.telelab.paperville.fragments.FragmentHelper;
import com.telelab.paperville.fragments.HomeFragment;
import com.telelab.paperville.fragments.SearchFragment;
import com.telelab.paperville.models.DataWrapper;
import com.telelab.paperville.models.Subject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener
         {
    private static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    private FragmentHelper fragmentHelper = new FragmentHelper();
    private Fragment homeFragment;
    private final Fragment bookmarkFragment = new BookmarkFragment();
    private final Fragment searchFragment = new SearchFragment();
    private Fragment active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataWrapper wrapper = (DataWrapper) getIntent().getSerializableExtra("subjects");
        homeFragment = new HomeFragment(wrapper.getSubjects());
        init();
        setListeners();

    }

    private void init(){
        bottomNavigationView = findViewById(R.id.botton_navigation_view);
        bottomNavigationView.getMenu().findItem(R.id.home_navigation).setChecked(true);
        bottomNavigationView.bringToFront();
        active = homeFragment;

        fragmentHelper.setHiddenFragment(bookmarkFragment, "bookmarkFrag", getSupportFragmentManager());
        fragmentHelper.setHiddenFragment(searchFragment, "searchFrag", getSupportFragmentManager());
        fragmentHelper.setCurrentFragment(homeFragment, "homeFrag", getSupportFragmentManager());

    }

    private void setListeners(){
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.search_navigation:
                        active = fragmentHelper.changeFragment(searchFragment, active, getSupportFragmentManager());
                        return true;

                    case R.id.home_navigation:
                        active = fragmentHelper.changeFragment(homeFragment, active, getSupportFragmentManager());
                        return true;

                    case R.id.bookmark_navigation:
                        active = fragmentHelper.changeFragment(bookmarkFragment, active, getSupportFragmentManager());
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        HelperMethods.pushNewRoutingActivity(this, SearchActivity.class);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }




}

