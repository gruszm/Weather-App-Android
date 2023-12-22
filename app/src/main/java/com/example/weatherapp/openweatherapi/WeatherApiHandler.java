package com.example.weatherapp.openweatherapi;

import static com.example.weatherapp.WeatherAppConfig.WEATHER_APP_RESPONSE_LANG;
import static com.example.weatherapp.WeatherAppConfig.WEATHER_APP_UNITS;

import android.content.Context;
import android.widget.Toast;

import com.example.weatherapp.BuildConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApiHandler
{
    public static void getWeatherResponseFromApi(String city, int count, WeatherResponseCallback callback, Context context)
    {
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
                WEATHER_APP_UNITS
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

            }
        });
    }
}
