package com.example.appxemphim.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.appxemphim.Fragment.HomeFragment;
import com.example.appxemphim.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.appxemphim.Fragment.SearchFragment;
import com.example.appxemphim.Fragment.SettingFragment;
import com.example.appxemphim.Fragment.UserProfileFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        replaceFragment(new HomeFragment());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home)
                replaceFragment(new HomeFragment());
            else if (item.getItemId() == R.id.navigation_search) {
                replaceFragment(new SearchFragment());
            } else if (item.getItemId() == R.id.navigation_profile) {
                replaceFragment(new UserProfileFragment());
            } else if (item.getItemId() == R.id.navigation_setting) {
                replaceFragment(new SettingFragment());
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragment,fragment);
        fragmentTransaction.commit();
    }
}