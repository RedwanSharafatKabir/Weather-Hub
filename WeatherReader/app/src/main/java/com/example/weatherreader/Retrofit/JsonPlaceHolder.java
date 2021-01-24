package com.example.weatherreader.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolder {

    @GET("weather?id=1337178&appid=372c08b8e0c21f52d218a9699b8da4c7")
    Call<Example> getWeatherData(@Query("q") String name);

}
