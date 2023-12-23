package com.example.weatherapp;

import static com.example.weatherapp.ForecastAdapter.FORECAST_NUM;
import static com.example.weatherapp.WeatherAppConfig.WEATHER_APP_SHARED_PREFS_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.weatherapp.openweatherapi.WeatherApiHandler;
import com.example.weatherapp.openweatherapi.WeatherResponse;
import com.example.weatherapp.openweatherapi.WeatherResponseCallback;

public class BasicInfoFragment extends Fragment
{
    private EditText cityET;
    private TextView cityNameTV, latitudeTV, longitudeTV, timeTV, pressureTv, temperatureTV, descriptionTV;
    private WeatherResponseCallback weatherResponseCallback;

    public BasicInfoFragment(WeatherResponseCallback weatherResponseCallback)
    {
        this.weatherResponseCallback = weatherResponseCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.basic_info_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        cityET = view.findViewById(R.id.city_name_edit_text);
        cityNameTV = view.findViewById(R.id.city_name_text_api_editable);
        latitudeTV = view.findViewById(R.id.latitude_text_editable);
        longitudeTV = view.findViewById(R.id.longitude_text_editable);
        timeTV = view.findViewById(R.id.time_text_editable);
        pressureTv = view.findViewById(R.id.pressure_text_editable);
        temperatureTV = view.findViewById(R.id.temperature_text_editable);
        descriptionTV = view.findViewById(R.id.description_text_editable);

        cityET.setOnEditorActionListener(this::onCityEditorAction);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(WEATHER_APP_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        cityET.setText(sharedPreferences.getString("current_city", ""));
    }

    boolean onCityEditorAction(TextView textView, int actionId, KeyEvent keyEvent)
    {
        if (actionId == EditorInfo.IME_ACTION_DONE)
        {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(WEATHER_APP_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("current_city", textView.getText().toString());
            editor.apply();

            WeatherApiHandler.getWeatherResponseFromApi(textView.getText().toString(), FORECAST_NUM, weatherResponseCallback, getContext());
        }

        return false;
    }

    public void updateWeatherInfo(WeatherResponse weatherResponse)
    {
        cityNameTV.setText(weatherResponse.getCityName());
        latitudeTV.setText(String.valueOf(weatherResponse.getLatitude()));
        longitudeTV.setText(String.valueOf(weatherResponse.getLongitude()));
        timeTV.setText(weatherResponse.getCurrentTimestamp());
        temperatureTV.setText(String.valueOf(weatherResponse.getCurrentTemperature()));
        pressureTv.setText(String.valueOf(weatherResponse.getCurrentPressure()));
        descriptionTV.setText(weatherResponse.getCurrentWeatherDescription());
    }
}
