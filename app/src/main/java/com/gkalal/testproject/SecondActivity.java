package com.gkalal.testproject;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SeekBar;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.gkalal.testproject.IRetrofit.BASE_URL;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    private SecondActivityBinder mBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mBinder = DataBindingUtil.setContentView(this, R.layout.activity_second);

            mBinder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progressValue = 0;

                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    try {
                        mBinder.textViewSeekbarValue.setText(String.valueOf(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            mBinder.buttonGetWeatherInfo.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonGetWeatherInfo:
                try {
                    hideKeyboard();
                    getWeatherInfo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void getWeatherInfo() {
        try {
            String cityName = mBinder.editTextCityName.getText().toString().trim();

            if (!TextUtils.isEmpty(cityName)) {
                callGetWeatherApi(cityName);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private void callGetWeatherApi(final String cityName) {
        try {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);

            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();

            IRetrofit iRetrofit = retrofit.create(IRetrofit.class);

            iRetrofit.getWeatherInfo(cityName).enqueue(new Callback<List<GetWeatherResponse>>() {
                @Override
                public void onResponse(Call<List<GetWeatherResponse>> call,
                                       Response<List<GetWeatherResponse>> response) {
                    try {
                        if (response.isSuccessful()) {
                            mBinder.textViewCityName.setText(cityName);
                            mBinder.textViewMinTemp.setText("Min Temp : " + response.body().get(0).getMin());
                            mBinder.textViewMaxTemp.setText("Max Temp : " + response.body().get(0).getMax());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<List<GetWeatherResponse>> call, Throwable t) {
                    try {
                        Log.d("OnFailure", t.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
