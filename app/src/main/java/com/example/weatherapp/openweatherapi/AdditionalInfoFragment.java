package com.example.weatherapp.openweatherapi;

import static com.example.weatherapp.WeatherAppConfig.WEATHER_APP_SHARED_PREFS_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.weatherapp.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

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
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(WEATHER_APP_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
            String units = sharedPreferences.getString("current_units", "Celsius");
            String speed;
            DecimalFormatSymbols spaceSeparator = new DecimalFormatSymbols();
            spaceSeparator.setGroupingSeparator(' ');
            DecimalFormat visibilityFormat = new DecimalFormat("#,###", spaceSeparator);

            switch (units)
            {
                case "Celsius":
                case "Kelvin":
                    speed = Math.round(weatherInfo.getCurrentWindSpeed() * 3.6F) + " kph"; // convert from m/s to km/h
                    break;
                case "Fahrenheit":
                default:
                    speed = Math.round(weatherInfo.getCurrentWindSpeed()) + " mph";
                    break;
            }

            cityNameTV.setText(weatherInfo.getCityName());
            windSpeedTV.setText(speed);
            windDirectionTV.setText(weatherInfo.getCurrentWindDirection());
            humidityTV.setText(String.valueOf(weatherInfo.getCurrentHumidity()).concat("%"));
            visibilityTV.setText(visibilityFormat.format(weatherInfo.getCurrentVisibility()).concat(" m"));
        }
        else
        {
            this.weatherInfo = weatherInfo;
        }
    }
}
