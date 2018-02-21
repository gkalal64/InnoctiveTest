package com.gkalal.testproject;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.gkalal.testproject.IRetrofit.BASE_URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MainActivityBinder mBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mBinder.buttonLogin.setOnClickListener(this);
    }

    private boolean isInputValidated() {
        String username = mBinder.editTextUsername.getText().toString();
        String password = mBinder.editTextPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            mBinder.editTextUsername.setError("Email is compulsory field");
            return false;
        } else if (TextUtils.isEmpty(password)) {
            mBinder.editTextPassword.setError("Password is compulsory field");
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLogin:
                if (isInputValidated()) {
                    hideKeyboard();
                    callLoginApi();
                }
                //startActivity(new Intent(MainActivity.this, SecondActivity.class));
                break;
        }
    }

    private void hideKeyboard() {
        try {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callLoginApi() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        IRetrofit iRetrofit = retrofit.create(IRetrofit.class);

        String username = mBinder.editTextUsername.getText().toString();
        String password = mBinder.editTextPassword.getText().toString();

        iRetrofit.loginUser(username, password).enqueue(new Callback<List<LoginResponse>>() {
            @Override
            public void onResponse(Call<List<LoginResponse>> call,
                                   Response<List<LoginResponse>> response) {
                try {
                    if (response.isSuccessful()) {
                        if (!TextUtils.isEmpty(response.body().get(0).getLoginId())) {
                            startActivity(new Intent(MainActivity.this, SecondActivity.class));
                        } else if (!TextUtils.isEmpty(response.body().get(0).getMessage())) {
                            Toast.makeText(MainActivity.this, response.body().get(0).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<LoginResponse>> call, Throwable t) {
                try {
                    Log.d("OnFailure", t.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
