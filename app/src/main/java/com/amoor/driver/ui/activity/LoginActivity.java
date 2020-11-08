package com.amoor.driver.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amoor.driver.R;
import com.amoor.driver.data.model.login.Login;
import com.amoor.driver.data.rest.ApiServices;
import com.amoor.driver.data.rest.RetrofitClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.amoor.driver.helper.HelperMethod.getTextFromTil;
import static com.amoor.driver.helper.HelperMethod.setTilEmpty;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_activity_til_student_id)
    TextInputLayout loginActivityTilStudentId;
    @BindView(R.id.login_activity_til_password)
    TextInputLayout loginActivityTilPassword;
    private ApiServices apiServices;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        preferences = getSharedPreferences("DriverData", Context.MODE_PRIVATE);
        apiServices = RetrofitClient.getClient().create(ApiServices.class);
        progressDialog = new ProgressDialog(this);


    }

    @OnClick(R.id.login_activity_btn_sign_in)
    public void onViewClicked() {
        login();
    }

    private void login() {
        String studentId = getTextFromTil(loginActivityTilStudentId);
        String password = getTextFromTil(loginActivityTilPassword);
        if (!TextUtils.isEmpty(studentId) && !TextUtils.isEmpty(password)) {

            loginRequest(studentId, password);

        } else {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
        }

    }

    private void loginRequest(String studentId, String password) {
        progressDialog.setMessage("انتظر قليلا يتم تسجيل الدخول ... ");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        apiServices.login(studentId, password).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        String accessToken = response.body().getAccessToken();
                        String driverId = response.body().getDriverId();
                        String driverName = response.body().getDriverName();

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("status", true);
                        editor.putString("access_token", accessToken);
                        editor.putString("driver_id", driverId);
                        editor.putString("driver_name", driverName);
                        editor.apply();

                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    } else {


                        Toast.makeText(LoginActivity.this, "wrong data " + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        setTilEmpty(loginActivityTilPassword);
                        setTilEmpty(loginActivityTilStudentId);

                    }

                } else {
                    Toast.makeText(LoginActivity.this, "onResponse: " + response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "onFailure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        progressDialog.dismiss();

    }
}
