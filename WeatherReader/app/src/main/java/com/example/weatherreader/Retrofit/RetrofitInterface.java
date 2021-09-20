package com.example.weatherreader.Retrofit;

import com.example.weatherreader.ModelClasses.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {

//    @GET("weather?id=1337178&appid=372c08b8e0c21f52d218a9699b8da4c7")
//    Call<Example> getWeatherData(@Query("q") String name);

    @GET("weather")
    Observable<WeatherResult> getWeatherLatLng(@Query("lat") String lat,
                                               @Query("lon") String lon,
                                               @Query("appid") String appid,
                                               @Query("units") String unit);

}
