package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.openweatherapi.WeatherResponse;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastElementViewHolder>
{
    public static final int FORECAST_NUM = 40;

    private WeatherResponse weatherResponse;

    public ForecastAdapter(WeatherResponse weatherResponse)
    {
        this.weatherResponse = weatherResponse;
    }

    @Override
    public ForecastElementViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_element, parent, false);

        return new ForecastElementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastElementViewHolder holder, int position)
    {
        holder.timeTV.setText(weatherResponse.getTimestamp(position));
        holder.temperatureTV.setText(String.valueOf(weatherResponse.getTemperature(position)));
        holder.pressureTV.setText(String.valueOf(weatherResponse.getPressure(position)));
        holder.descTV.setText(weatherResponse.getWeatherDescription(position));
    }

    @Override
    public int getItemCount()
    {
        return FORECAST_NUM;
    }
}
