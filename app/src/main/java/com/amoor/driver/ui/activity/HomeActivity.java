package com.amoor.driver.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.amoor.driver.R;
import com.amoor.driver.adapter.MyPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.home_activity_tab)
    TabLayout homeActivityTab;
    @BindView(R.id.home_activity_pager)
    ViewPager homeActivityPager;
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.Main_Activity_Srl_Swipe)
    SwipeRefreshLayout MainActivitySrlSwipe;
    private SharedPreferences preferences;
    private static final String PHONE_CALL = Manifest.permission.CALL_PHONE;
    private boolean aPhoneCallPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        giveCallPhonePermission();
        ButterKnife.bind(this);
        setSupportActionBar(mainToolbar);
        preferences = getSharedPreferences("DriverData", Context.MODE_PRIVATE);


        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        homeActivityPager.setAdapter(adapter);
        homeActivityTab.setupWithViewPager(homeActivityPager);

        MainActivitySrlSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                int selectedTabPosition = homeActivityTab.getSelectedTabPosition();
                MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
                homeActivityPager.setAdapter(adapter);
                homeActivityPager.setCurrentItem(selectedTabPosition);
                homeActivityTab.setupWithViewPager(homeActivityPager);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivitySrlSwipe.setRefreshing(false);
                    }
                }, 2000);
            }


        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("access_token", "");
            editor.putString("driver_id", "");
            editor.putBoolean("status", false);
            editor.apply();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();

        }
        return true;
    }

    private void giveCallPhonePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(PHONE_CALL) != PackageManager.PERMISSION_GRANTED) {
                if (!aPhoneCallPermissionGranted) {
                    requestPermissions(new String[]{PHONE_CALL}, 10);
                }

            }


        }

    }
}
