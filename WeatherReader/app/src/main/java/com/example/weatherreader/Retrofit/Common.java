package com.example.weatherreader.Retrofit;

import android.location.Location;

import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static final String APP_ID = "a8d0ea13948b5cf59bcfc02e1b0f3a05";
    public static Task<Location> CURRENT_LOCATION = null;

    public static String convertUnixToHour(int hour){
        Date date = new Date(hour*1000L);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String formatedDate = simpleDateFormat.format(date);

        return formatedDate;
    }
}
