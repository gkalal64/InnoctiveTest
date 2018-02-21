package com.gkalal.testproject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IRetrofit {

    String BASE_URL = "http://trucklo-test.herokuapp.com/";

    @POST("newlogin/{email}/{password}")
    Call<List<LoginResponse>> loginUser(@Path("email") String email, @Path("password") String password);

    @GET("getweatherinfo/api/v1/{city}")
    Call<List<GetWeatherResponse>> getWeatherInfo(@Path("city") String city);
}