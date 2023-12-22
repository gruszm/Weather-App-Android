package com.example.weatherapp.openweatherapi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService
{
    @GET("forecast")
    Call<WeatherResponse> getCurrentWeather(
            @Query("q") String city,
            @Query("appid") String apiKey,
            @Query("cnt") int count,
            @Query("lang") String language,
            @Query("units") String units
    );
}
