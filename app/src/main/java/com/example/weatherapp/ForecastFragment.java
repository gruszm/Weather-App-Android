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
    private WeatherResponse weatherInfoHistory;
    private String temperatureSuffix;

    public ForecastFragment(WeatherResponse weatherInfoHistory, String temperatureSuffix)
    {
        this.weatherInfoHistory = weatherInfoHistory;
        this.temperatureSuffix = temperatureSuffix;
    }

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
        recyclerView.setAdapter(forecastAdapter);
        updateWeatherInfo(weatherInfoHistory, temperatureSuffix);
    }

    public void updateWeatherInfo(WeatherResponse weatherResponse, String temperatureSuffix)
    {
        if (forecastAdapter != null)
        {
            forecastAdapter.updateWeatherInfo(weatherResponse, temperatureSuffix);
            forecastAdapter.notifyDataSetChanged();
        }
        else
        {
            this.weatherInfoHistory = weatherResponse;
        }
    }
}
