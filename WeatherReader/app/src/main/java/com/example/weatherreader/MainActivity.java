package com.example.weatherreader;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.weatherreader.Retrofit.ApiClient;
import com.example.weatherreader.Retrofit.Example;
import com.example.weatherreader.Retrofit.JsonPlaceHolder;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    TextInputEditText textInputEditText;
    TextView place, temperature, description, pressure, humidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = findViewById(R.id.linearLayoutId);
        textInputEditText = findViewById(R.id.searchId);
        place = findViewById(R.id.placeNameId);
        temperature = findViewById(R.id.temperatureId);
        description = findViewById(R.id.feelsLikeId);
        pressure = findViewById(R.id.pressureId);
        humidity = findViewById(R.id.humidityId);
    }

    public void enterPlaceName(View v){
        place.setText("");
        temperature.setText("");
        description.setText("");
        pressure.setText("");
        humidity.setText("");

        String cityName = textInputEditText.getText().toString();
        if(!cityName.equals("")){
            getWeatherData(cityName);
        } else {
            place.setText("");
            temperature.setText("");
            description.setText("");
            pressure.setText("");
            humidity.setText("");
        }
    }

    public void getWeatherData(String name){
        JsonPlaceHolder jsonPlaceHolder = ApiClient.getClient().create(JsonPlaceHolder.class);

        Call<Example> call = jsonPlaceHolder.getWeatherData(name);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                linearLayout.setVisibility(View.VISIBLE);
                place.setText("City: " + name);
                temperature.setText("Temperature: " + response.body().getMain().getTemp() + "° kelvin");
                description.setText("Feels like: " + response.body().getMain().getFeels_like());
                pressure.setText("Pressure in PSI: " + response.body().getMain().getPressure() + " inHg");
                humidity.setText("Humidity: " + response.body().getMain().getHumidity());
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                place.setText(t.getMessage());
                temperature.setText("");
                description.setText("");
                pressure.setText("");
                humidity.setText("");
            }
        });
    }
}
