package com.example.weatherreader.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

public class FragmentTwo extends Fragment {

    Snackbar snackbar;
    View views, parentLayout;
    ProgressBar progressBar;
    TextView temperatureC, pressure, humidity, time, date, wind, sunrise, sunset, geoCoord, maxTemp, minTemp;
    private FusedLocationProviderClient fusedLocationProviderClient;
    String currentDate, currentTime, latitude = "", longitude = "", currentPlace;
    CompositeDisposable compositeDisposable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.fragment_fragment_two, container, false);

        parentLayout = views.findViewById(android.R.id.content);
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
        geoCoord = views.findViewById(R.id.geoCoordId);

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        requestLocation();

        return views;
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

            getCurrentLocation();

        } else {
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        Date cal = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yyyy");
        currentDate = simpleDateFormat1.format(cal);

        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm:ss aaa");
        currentTime = simpleDateFormat2.format(new Date());

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestLocation();
        }

        fusedLocationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Location permission denied !", Toast.LENGTH_LONG).show();
            }

        }).addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }

                    Common.CURRENT_LOCATION = fusedLocationProviderClient.getLastLocation();

                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());

                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    List<Address> addressList;

                    try {
                        addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        currentPlace = addressList.get(0).getLocality();

                        getWeatherData(currentPlace);

                        time.setText(" " + currentTime);
                        date.setText(" " + currentDate);

                        BigDecimal v1 = new BigDecimal(latitude).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal v2 = new BigDecimal(longitude).setScale(2, RoundingMode.HALF_UP);
                        double newInput1 = v1.doubleValue();
                        double newInput2 = v2.doubleValue();

                        geoCoord.setText("Location: [" + newInput1 + "]" + ", [" + newInput2 + "]");

                    } catch (IOException e) {
                        Log.i("ERROR ", "Permission Denied");
                        Toast.makeText(getActivity(), "Location permission denied !", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }

    public FragmentTwo(){
        compositeDisposable = new CompositeDisposable();
    }

    public void getWeatherData(String name){
        RetrofitInterface retrofitInterface = RetrofitClient.getInstance().create(RetrofitInterface.class);

        compositeDisposable.add(retrofitInterface.getWeatherLatLng(latitude, longitude, Common.APP_ID, "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {
                        temperatureC.setText(name + ", " + weatherResult.getMain().getTemp() + "° C");
                        maxTemp.setText("Highest temp: " + weatherResult.getMain().getTemp_max() + "° C");
                        minTemp.setText("Lowest temp: " + weatherResult.getMain().getTemp_min() + "° C");
                        pressure.setText("Pressure: " + weatherResult.getMain().getPressure());
                        humidity.setText("Humidity: " + weatherResult.getMain().getHumidity());
                        wind.setText("Wind speed: " + weatherResult.getWind().getSpeed() + ", Deg: " + weatherResult.getWind().getDeg());
                        sunrise.setText("Sunrise: " + Common.convertUnixToHour(weatherResult.getSys().getSunrise()));
                        sunset.setText("Sunset: " + Common.convertUnixToHour(weatherResult.getSys().getSunset()));
                        sunset.setText("Sunset: " + Common.convertUnixToHour(1645896325));

                        progressBar.setVisibility(View.GONE);
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("Error ", throwable.getMessage());
                        progressBar.setVisibility(View.GONE);
                    }
                })
        );

/*
        Call<WeatherResult> call = (Call<WeatherResult>) retrofitInterface.getWeatherLatLng(latitude, longitude, Common.APP_ID, "metric");
        call.enqueue(new Callback<WeatherResult>() {
            @Override
            public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {
                try {
                    String kelvin = String.valueOf(response.body().getMain().getTemp());
                    double celsius = Double.parseDouble(kelvin) - 273.15;

                    BigDecimal bd = new BigDecimal(celsius).setScale(2, RoundingMode.HALF_UP);
                    double newInput = bd.doubleValue();
                    temperatureC.setText(currentPlace + newInput + "° celsius");

                    pressure.setText("Pressure in PSI: " + response.body().getMain().getPressure() + " inHg");
                    humidity.setText("Humidity: " + response.body().getMain().getHumidity());
                    maxTemp.setText("Highest temp: " + response.body().getMain().getTemp_max() + "° C");
                    minTemp.setText("Lowest temp: " + response.body().getMain().getTemp_min() + "° C");

                    progressBar.setVisibility(View.GONE);

                } catch (Exception e){
                    progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<WeatherResult> call, Throwable t) {
                snackbar = Snackbar.make(parentLayout, "Turn on internet connection", Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Red));
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                snackbar.setDuration(10000).show();
                progressBar.setVisibility(View.GONE);

            }
        });
 */
    }
}
