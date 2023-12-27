package com.example.weatherapp;

import static com.example.weatherapp.ForecastAdapter.FORECAST_NUM;
import static com.example.weatherapp.WeatherAppConfig.WEATHER_APP_SHARED_PREFS_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weatherapp.openweatherapi.AdditionalInfoFragment;
import com.example.weatherapp.openweatherapi.WeatherApiHandler;
import com.example.weatherapp.openweatherapi.WeatherResponse;
import com.example.weatherapp.openweatherapi.WeatherResponseCallback;
import com.google.gson.Gson;

public class WeatherInfoPagerActivity extends FragmentActivity
{
    private static final int NUM_PAGES = 3;

    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    private SwipeRefreshLayout swipeToRefreshWeatherDataLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);

        swipeToRefreshWeatherDataLayout = findViewById(R.id.swipe_to_refresh_weather_data_layout);
        swipeToRefreshWeatherDataLayout.setOnRefreshListener(this::weatherDataOnRefreshListener);

        viewPager = findViewById(R.id.pager);
        pagerAdapter = new WeatherInfoPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
    }

    private void weatherDataOnRefreshListener()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(WEATHER_APP_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        WeatherApiHandler.getWeatherResponseFromApi(sharedPreferences.getString("current_city", ""), FORECAST_NUM, (WeatherResponseCallback) pagerAdapter, getApplicationContext());
        swipeToRefreshWeatherDataLayout.setRefreshing(false);
    }

    private class WeatherInfoPagerAdapter extends FragmentStateAdapter implements WeatherResponseCallback
    {
        private BasicInfoFragment basicInfoFragment;
        private AdditionalInfoFragment additionalInfoFragment;
        private ForecastFragment forecastFragment;
        private String temperatureSuffix;

        public WeatherInfoPagerAdapter(FragmentActivity fragmentActivity)
        {
            super(fragmentActivity);

            SharedPreferences sharedPreferences = getSharedPreferences(WEATHER_APP_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String weatherInfoSerialized = sharedPreferences.getString("weather_info_history", "");
            WeatherResponse weatherInfoHistory = gson.fromJson(weatherInfoSerialized, WeatherResponse.class);

            updateTemperatureSuffix();

            basicInfoFragment = new BasicInfoFragment(weatherInfoHistory, temperatureSuffix, this);
            additionalInfoFragment = new AdditionalInfoFragment(weatherInfoHistory);
            forecastFragment = new ForecastFragment(weatherInfoHistory, temperatureSuffix);

            // check, if a history exists or if first forecast in history is older than current system time
            if ((weatherInfoHistory == null) || (System.currentTimeMillis() / 1000) > weatherInfoHistory.getOldestDataTime())
            {
                WeatherApiHandler.getWeatherResponseFromApi(sharedPreferences.getString("current_city", ""), FORECAST_NUM, this, getApplicationContext());
            }
        }

        @Override
        public Fragment createFragment(int position)
        {
            switch (position)
            {
                case 0:
                    return basicInfoFragment;
                case 1:
                    return additionalInfoFragment;
                case 2:
                default:
                    return forecastFragment;
            }
        }

        @Override
        public int getItemCount()
        {
            return NUM_PAGES;
        }

        @Override
        public void onWeatherInfoUpdate(WeatherResponse weatherResponse)
        {
            updateTemperatureSuffix();

            basicInfoFragment.updateWeatherInfo(weatherResponse, temperatureSuffix);
            additionalInfoFragment.updateWeatherInfo(weatherResponse);
            forecastFragment.updateWeatherInfo(weatherResponse, temperatureSuffix);

            SharedPreferences sharedPreferences = getSharedPreferences(WEATHER_APP_SHARED_PREFS_NAME, MODE_PRIVATE);
            Gson gson = new Gson();
            String weatherInfoSerialized = gson.toJson(weatherResponse);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("weather_info_history", weatherInfoSerialized);
            editor.apply();
        }

        private void updateTemperatureSuffix()
        {
            SharedPreferences sharedPreferences = getSharedPreferences(WEATHER_APP_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
            String units = sharedPreferences.getString("current_units", "Celsius");

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
        }
    }
}
