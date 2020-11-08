package com.amoor.driver.ui.activity.noInternet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;

import com.amoor.driver.R;
import com.amoor.driver.ui.activity.HomeActivity;
import com.amoor.driver.ui.activity.LoginActivity;


import butterknife.BindView;
import butterknife.ButterKnife;

public class NoInternetActivity extends AppCompatActivity {

//    @BindView(R.id.NoInternet_Activity_Iv_Try_Again)
//    ImageView NoInternetActivityIvTryAgain;
    @BindView(R.id.NoInternet_Activity_Srl_Swipe)
    SwipeRefreshLayout NoInternetActivitySrlSwipe;
    private SharedPreferences preferences;
    private boolean status;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        ButterKnife.bind(this);

        preferences = getSharedPreferences("DriverData", Context.MODE_PRIVATE);
        status = preferences.getBoolean("status", false);

        NoInternetActivitySrlSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checkInternet()) {
                    if (status) {
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NoInternetActivitySrlSwipe.setRefreshing(false);
                    }
                }, 2000);
            }
        });

    }

    public boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else {
            return false;

        }
    }

//    @OnClick(R.id.NoInternet_Activity_Iv_Try_Again)
//    public void onViewClicked() {
//        animation = AnimationUtils.loadAnimation(this, android.R.anim.accelerate_interpolator);
//        animation.setDuration(2000);
//        NoInternetActivityIvTryAgain.startAnimation(animation);
//
//        if (checkInternet()) {
//            if (status) {
//                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//                finish();
//            } else {
//                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                finish();
//            }
//        } else {
//            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
//        }
//
//    }
}

