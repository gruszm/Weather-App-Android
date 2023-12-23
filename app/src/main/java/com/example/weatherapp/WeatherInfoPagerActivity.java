package com.example.weatherapp;

import static com.example.weatherapp.ForecastAdapter.FORECAST_NUM;
import static com.example.weatherapp.WeatherAppConfig.WEATHER_APP_SHARED_PREFS_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weatherapp.openweatherapi.WeatherApiHandler;
import com.example.weatherapp.openweatherapi.WeatherResponse;
import com.example.weatherapp.openweatherapi.WeatherResponseCallback;

public class WeatherInfoPagerActivity extends FragmentActivity
{
    private static final int NUM_PAGES = 3;

    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);

        viewPager = findViewById(R.id.pager);
        pagerAdapter = new WeatherInfoPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
    }

    private class WeatherInfoPagerAdapter extends FragmentStateAdapter implements WeatherResponseCallback
    {
        private BasicInfoFragment basicInfoFragment;
        private WeatherInfoFragment weatherInfoFragment;
        private ForecastFragment forecastFragment;

        public WeatherInfoPagerAdapter(FragmentActivity fragmentActivity)
        {
            super(fragmentActivity);

            basicInfoFragment = new BasicInfoFragment(this);
            weatherInfoFragment = new WeatherInfoFragment();
            forecastFragment = new ForecastFragment();

            SharedPreferences sharedPreferences = getSharedPreferences(WEATHER_APP_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
            WeatherApiHandler.getWeatherResponseFromApi(sharedPreferences.getString("current_city", ""), FORECAST_NUM, this, getApplicationContext());
        }

        @Override
        public Fragment createFragment(int position)
        {
            switch (position)
            {
                case 0:
                    return basicInfoFragment;
                case 1:
                    return weatherInfoFragment;
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
            basicInfoFragment.updateWeatherInfo(weatherResponse);
            forecastFragment.updateWeatherInfo(weatherResponse);
        }
    }
}
