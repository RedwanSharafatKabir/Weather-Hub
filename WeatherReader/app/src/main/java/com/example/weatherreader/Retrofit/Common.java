package com.example.weatherreader.Retrofit;

import android.location.Location;

import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static final String APP_ID = "3b8236b9c5ed70aba67745d671025ac8";
    public static Task<Location> CURRENT_LOCATION = null;

    public static String convertUnixToDate(long dt){
        Date date = new Date(dt*1000L);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd EEE MM yyyy");
        String formatedDate = simpleDateFormat.format(date);

        return formatedDate;
    }

    public static String convertUnixToHour(int hour){
        Date date = new Date(hour*1000L);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String formatedDate = simpleDateFormat.format(date);

        return formatedDate;
    }
}
