package com.example.weatherreader.Retrofit;

import com.example.weatherreader.ModelClasses.WeatherForecastResult;
import com.example.weatherreader.ModelClasses.WeatherResult;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {

    @GET("weather")
    Observable<WeatherResult> getWeatherByLatLng(@Query("lat") String lat,
                                               @Query("lon") String lon,
                                               @Query("appid") String appid,
                                               @Query("units") String unit);

    @GET("forecast")
    Observable<WeatherForecastResult> getWeatherForecastByLatLng(@Query("lat") String lat,
                                                       @Query("lon") String lon,
                                                       @Query("appid") String appid,
                                                       @Query("units") String unit);

    @GET("weather")
    Observable<WeatherResult> getWeatherByCity(@Query("q") String cityName,
                                                         @Query("appid") String appid,
                                                         @Query("units") String unit);

}
