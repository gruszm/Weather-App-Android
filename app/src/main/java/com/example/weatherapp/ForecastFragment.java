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

import com.example.weatherapp.openweatherapi.WeatherApiHandler;
import com.example.weatherapp.openweatherapi.WeatherResponse;
import com.example.weatherapp.openweatherapi.WeatherResponseCallback;

public class ForecastFragment extends Fragment implements WeatherResponseCallback
{
    private RecyclerView recyclerView;
    private ForecastAdapter forecastAdapter;

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

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(WEATHER_APP_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        WeatherApiHandler.getWeatherResponseFromApi(sharedPreferences.getString("current_city", ""), 40, this, this.getContext());
    }

    @Override
    public void onWeatherInfoUpdate(WeatherResponse weatherResponse)
    {
        forecastAdapter = new ForecastAdapter(weatherResponse);
        recyclerView.setAdapter(forecastAdapter);
    }
}
