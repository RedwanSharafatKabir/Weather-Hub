package com.example.weatherreader.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.weatherreader.AppAction.MainActivity;
import com.example.weatherreader.BackPageInterface.BackListenerFragment;
import com.example.weatherreader.ModelClasses.WeatherResult;
import com.example.weatherreader.R;
import com.example.weatherreader.Retrofit.Common;
import com.example.weatherreader.Retrofit.RetrofitClient;
import com.example.weatherreader.Retrofit.RetrofitInterface;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentTwo extends Fragment implements View.OnClickListener, BackListenerFragment {

    View views;
    ProgressBar progressBar;
    private Snackbar snackbar;
    private ConnectivityManager cm;
    private NetworkInfo netInfo;
    public static BackListenerFragment backBtnListener;
    TextView temperatureC, pressure, humidity, time, date, wind, sunrise, sunset;
    TextView maxTemp, minTemp, nextTenDays;
    String currentDate="", currentTime="", cityValue="";
    CompositeDisposable compositeDisposable;
    private Spinner citySpinner;
    String city[];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.fragment_fragment_two, container, false);

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        progressBar = views.findViewById(R.id.progressBarId);

        time = views.findViewById(R.id.timeId);
        date = views.findViewById(R.id.dateId);
        temperatureC = views.findViewById(R.id.temperatureCId);
        pressure = views.findViewById(R.id.pressureId);
        humidity = views.findViewById(R.id.humidityId);
        wind = views.findViewById(R.id.windId);
        sunrise = views.findViewById(R.id.sunriseId);
        sunset = views.findViewById(R.id.sunsetId);
        maxTemp = views.findViewById(R.id.maxTempId);
        minTemp = views.findViewById(R.id.minTempId);

        nextTenDays = views.findViewById(R.id.nextTenDaysId);
        nextTenDays.setOnClickListener(this);

        city = getResources().getStringArray(R.array.city_array);
        citySpinner = views.findViewById(R.id.citySpinnerId);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.city_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            getWeatherData(cityMethod());

        } else {
            snackbar = Snackbar.make(views, "Turn on internet connection", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Red));
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
            snackbar.setDuration(10000).show();

            progressBar.setVisibility(View.GONE);
        }

        return views;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nextTenDaysId:
                ((MainActivity) getActivity()).viewPager2.setCurrentItem(1);

                break;
        }
    }

    private String cityMethod(){
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityValue = city[position];

                getWeatherData(cityMethod());

                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setTextSize(18);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return cityValue.toLowerCase();
    }

    public FragmentTwo(){
        compositeDisposable = new CompositeDisposable();
    }

    public void getWeatherData(String cityValue){
        Date cal = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yyyy");
        currentDate = simpleDateFormat1.format(cal);

        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm:ss aaa");
        currentTime = simpleDateFormat2.format(new Date());

        time.setText(" " + currentTime);
        date.setText(" " + currentDate);

        RetrofitInterface retrofitInterface = RetrofitClient.getInstance().create(RetrofitInterface.class);

        // GET Weather Information by City name
        compositeDisposable.add(retrofitInterface.getWeatherByCity(cityValue, Common.APP_ID, "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {

                        temperatureC.setText(cityValue + ", " + weatherResult.getMain().getTemp() + "° C");
                        maxTemp.setText("Highest temp: " + weatherResult.getMain().getTemp_max() + "° C");
                        minTemp.setText("Lowest temp: " + weatherResult.getMain().getTemp_min() + "° C");
                        pressure.setText("Pressure: " + weatherResult.getMain().getPressure());
                        humidity.setText("Humidity: " + weatherResult.getMain().getHumidity());
                        wind.setText("Wind speed: " + weatherResult.getWind().getSpeed() + ", Deg: " + weatherResult.getWind().getDeg());
                        sunrise.setText("Sunrise: " + Common.convertUnixToHour(weatherResult.getSys().getSunrise()));
                        sunset.setText("Sunset: " + Common.convertUnixToHour(weatherResult.getSys().getSunset()));

                        progressBar.setVisibility(View.GONE);
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("Error_Response ", throwable.getMessage());
                        progressBar.setVisibility(View.GONE);
                    }
                })
        );
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        backBtnListener = this;
    }

    @Override
    public void onPause() {
        backBtnListener = null;
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("EXIT !");
        alertDialogBuilder.setMessage("Are you sure you want to close this app ?");
        alertDialogBuilder.setIcon(R.drawable.exit);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
                getActivity().finishAffinity();
            }
        });

        alertDialogBuilder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
