package com.example.weatherapp.openweatherapi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.weatherapp.R;

public class AdditionalInfoFragment extends Fragment
{
    private WeatherResponse weatherInfo;
    public TextView cityNameTV, windSpeedTV, windDirectionTV, humidityTV, visibilityTV;
    private boolean viewCreated = false;

    public AdditionalInfoFragment(WeatherResponse weatherInfo)
    {
        this.weatherInfo = weatherInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.additional_info_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        cityNameTV = view.findViewById(R.id.additional_info_city_name);
        windSpeedTV = view.findViewById(R.id.additional_info_wind_speed_tv_editable);
        windDirectionTV = view.findViewById(R.id.additional_info_wind_direction_tv_editable);
        humidityTV = view.findViewById(R.id.additional_info_humidity_tv_editable);
        visibilityTV = view.findViewById(R.id.additional_info_visibility_tv_editable);

        viewCreated = true;

        updateWeatherInfo(weatherInfo);
    }

    public void updateWeatherInfo(WeatherResponse weatherInfo)
    {
        if (viewCreated && (weatherInfo != null))
        {
            cityNameTV.setText(weatherInfo.getCityName());
            windSpeedTV.setText(String.valueOf(weatherInfo.getCurrentWindSpeed()));
            windDirectionTV.setText(String.valueOf(weatherInfo.getCurrentWindDirection()));
            humidityTV.setText(String.valueOf(weatherInfo.getCurrentHumidity()));
            visibilityTV.setText(String.valueOf(weatherInfo.getCurrentVisibility()));
        }
        else
        {
            this.weatherInfo = weatherInfo;
        }
    }
}
