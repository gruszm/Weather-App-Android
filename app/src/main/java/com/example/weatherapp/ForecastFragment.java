package com.example.weatherapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.openweatherapi.WeatherResponse;

public class ForecastFragment extends Fragment
{
    private RecyclerView recyclerView;
    private ForecastAdapter forecastAdapter;
    private WeatherResponse weatherResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.forecast_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.forecast_fragment_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        forecastAdapter = new ForecastAdapter();
        forecastAdapter.updateWeatherInfo(weatherResponse);
        recyclerView.setAdapter(forecastAdapter);
    }

    public void updateWeatherInfo(WeatherResponse weatherResponse)
    {
        if (forecastAdapter != null)
        {
            forecastAdapter.updateWeatherInfo(weatherResponse);
            forecastAdapter.notifyDataSetChanged();
        }
        else
        {
            this.weatherResponse = weatherResponse;
        }
    }
}
