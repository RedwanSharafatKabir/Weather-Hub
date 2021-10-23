package com.example.weatherreader.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.ContentCaptureCondition;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherreader.ModelClasses.Weather;
import com.example.weatherreader.ModelClasses.WeatherForecastResult;
import com.example.weatherreader.R;
import com.example.weatherreader.Retrofit.Common;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.MyViewHolder> {

    Context context;
    WeatherForecastResult weatherForecastResult;

    public ForecastAdapter(Context context, WeatherForecastResult weatherForecastResult) {
        this.context = context;
        this.weatherForecastResult = weatherForecastResult;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.forecast_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.date.setText(new StringBuilder(Common.convertUnixToDate(weatherForecastResult.getList().get(position).getDt())));
        holder.desc.setText(new StringBuilder(weatherForecastResult.getList().get(position).getWeather().get(0).getDescription()));
        holder.temp.setText(new StringBuilder(String.valueOf(weatherForecastResult.getList().get(position).getMain().getTemp())).append("° C"));
    }

    @Override
    public int getItemCount() {
        return weatherForecastResult.getList().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView date, desc, temp;

        public MyViewHolder (View itemView){
            super(itemView);

            date = itemView.findViewById(R.id.txtDateId);
            desc = itemView.findViewById(R.id.txtDescId);
            temp = itemView.findViewById(R.id.tempForecastId);
        }
    }
}
