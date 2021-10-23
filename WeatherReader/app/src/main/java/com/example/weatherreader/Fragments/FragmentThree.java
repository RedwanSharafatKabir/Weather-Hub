package com.example.weatherreader.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weatherreader.Adapters.ForecastAdapter;
import com.example.weatherreader.AppAction.MainActivity;
import com.example.weatherreader.BackPageInterface.BackListenerFragment;
import com.example.weatherreader.ModelClasses.WeatherForecastResult;
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
import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FragmentThree extends Fragment implements BackListenerFragment, View.OnClickListener {

    View views;
    TextView today;
    public static BackListenerFragment backBtnListener;
    static FragmentThree instance;
    CompositeDisposable compositeDisposable;
    RecyclerView recyclerView;
    private ConnectivityManager cm;
    private NetworkInfo netInfo;
    Snackbar snackbar;
    ProgressBar progressBar;
    private Parcelable recyclerViewState;
    ForecastAdapter forecastAdapter;
    private FusedLocationProviderClient fusedLocationProviderClient;
    String latitude = "", longitude = "", currentPlace;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.fragment_fragment_three, container, false);

        today = views.findViewById(R.id.todayIdFromNext);
        today.setOnClickListener(this);

        progressBar = views.findViewById(R.id.forecastProgressId);

        recyclerView = views.findViewById(R.id.foreCastRecyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
            }
        });

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            requestLocation();

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

                        getWeatherData();

                        BigDecimal v1 = new BigDecimal(latitude).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal v2 = new BigDecimal(longitude).setScale(2, RoundingMode.HALF_UP);

                    } catch (IOException e) {
                        Log.i("ERROR ", "Permission Denied");
                        Toast.makeText(getActivity(), "Location permission denied !", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }

    public static FragmentThree getInstance(){
        if(instance==null){
            instance = new FragmentThree();
        }

        return instance;
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    public FragmentThree(){
        compositeDisposable = new CompositeDisposable();
    }

    private void getWeatherData() {
        RetrofitInterface retrofitInterface = RetrofitClient.getInstance().create(RetrofitInterface.class);

        compositeDisposable.add(retrofitInterface.getWeatherForecastByLatLng(latitude, longitude, Common.APP_ID, "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecastResult>() {
                    @Override
                    public void accept(WeatherForecastResult weatherForecastResult) throws Exception {
                        displayWeatherDetails(weatherForecastResult);
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

    private void displayWeatherDetails(WeatherForecastResult weatherForecastResult) {
        forecastAdapter = new ForecastAdapter(getContext(), weatherForecastResult);
        recyclerView.setAdapter(forecastAdapter);
        forecastAdapter.notifyDataSetChanged();
        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.todayIdFromNext:
                ((MainActivity) getActivity()).viewPager2.setCurrentItem(0);

                break;
        }
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
        ((MainActivity) getActivity()).viewPager2.setCurrentItem(0);
    }
}
