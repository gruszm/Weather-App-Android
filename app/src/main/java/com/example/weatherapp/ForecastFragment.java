package com.example.weatherapp;

import static com.example.weatherapp.WeatherAppConfig.WEATHER_APP_SHARED_PREFS_NAME;

import android.content.Context;
import android.content.SharedPreferences;
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

    public ForecastFragment(WeatherResponse weatherInfoHistory)
    {
        this.weatherInfoHistory = weatherInfoHistory;
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
        updateWeatherInfo(weatherInfoHistory);
    }

    public void updateWeatherInfo(WeatherResponse weatherResponse)
    {
        if (forecastAdapter != null)
        {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(WEATHER_APP_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
            String units = sharedPreferences.getString("current_units", "Celsius");
            String temperatureSuffix;

            switch (units)
            {
                case "Celsius":
                    temperatureSuffix = " °C";
                    break;
                case "Fahrenheit":
                    temperatureSuffix = " °F";
                    break;
                case "Kelvin":
                default:
                    temperatureSuffix = " K";
                    break;
            }

            forecastAdapter.updateWeatherInfo(weatherResponse, temperatureSuffix);
            forecastAdapter.notifyDataSetChanged();
        }
        else
        {
            this.weatherInfoHistory = weatherResponse;
        }
    }
}
