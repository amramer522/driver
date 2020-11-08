package com.amoor.driver.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.amoor.driver.R;
import com.amoor.driver.ui.activity.noInternet.NoInternetActivity;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        preferences = getSharedPreferences("DriverData", Context.MODE_PRIVATE);
        final boolean status = preferences.getBoolean("status", false);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    if (status) {
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                } else {
//                    connected = false;
                    Toast.makeText(SplashActivity.this, "مفيش نت ياغالى ", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), NoInternetActivity.class));
                    finish();
//
                }

            }
        }, 1000);

    }
}
