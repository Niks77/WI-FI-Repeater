package com.example.wifidirect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;


import com.example.wifidirect.fragment.ClientFragment;
import com.example.wifidirect.fragment.HomeFragment;

import com.example.wifidirect.direct.DirectNetShare;
import com.example.wifidirect.fragment.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    private BottomNavigationView bottomNavigationView;
    private final Fragment homeFragment = new HomeFragment();
    private final Fragment clientFragment= new ClientFragment();
    private final Fragment settingFragment = new SettingFragment();
    private  Fragment active = homeFragment;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private Context context = this;
    private boolean isInitiated = false;
    private String fragTag;
    private String prevfragTag;
    private Fragment restoreFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        if(savedInstanceState == null) {
            fragmentManager.beginTransaction().replace(R.id.container, homeFragment).commit();
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");

    }
//
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("fragTag",fragTag);
        super.onSaveInstanceState(outState);
    }

    private void init()  {
        bottomNavigationView = findViewById(R.id.navigaion);
    }



    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Log.d(TAG, "onNavigationItemSelected: " + item.getItemId() + " " + R.id.navigation_home);
            switch (item.getItemId()){
                case R.id.navigation_home:
                    fragmentManager.beginTransaction().replace(R.id.container,homeFragment).commit();
                    return true;
                case R.id.navigation_client:
                    fragmentManager.beginTransaction().replace(R.id.container,clientFragment).commit();
                    return true;
                case R.id.navigation_server:
                    fragmentManager.beginTransaction().replace(R.id.container,settingFragment).commit();
                    return true;
            }
            return false;
        }
    };

}

