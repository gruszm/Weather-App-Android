package com.example.weatherapp.openweatherapi;

import static com.example.weatherapp.WeatherAppConfig.WEATHER_APP_RESPONSE_LANG;
import static com.example.weatherapp.WeatherAppConfig.WEATHER_APP_SHARED_PREFS_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.weatherapp.BuildConfig;

import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApiHandler
{
    public static void getWeatherResponseFromApi(String city, int count, WeatherResponseCallback callback, Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(WEATHER_APP_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String units = sharedPreferences.getString("current_units", "Celsius");

        units = units.equals("Celsius") ? "metric" : units;
        units = units.equals("Fahrenheit") ? "imperial" : units;
        units = units.equals("Kelvin") ? "standard" : units;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService weatherService = retrofit.create(WeatherService.class);

        Call<WeatherResponse> call = weatherService.getCurrentWeather(
                city,
                BuildConfig.OPEN_WEATHER_API_KEY,
                count,
                WEATHER_APP_RESPONSE_LANG,
                units
        );

        call.enqueue(new Callback<WeatherResponse>()
        {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response)
            {
                if (response.body() != null)
                {
                    callback.onWeatherInfoUpdate(response.body());
                }
                else
                {
                    Toast.makeText(context, "Could not retrieve weather info for this city", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t)
            {
                if (t.getClass() == UnknownHostException.class)
                {
                    Toast.makeText(context, "No internet connection. The weather data might be outdated.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "Please try again or refresh when the connection is restored.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
