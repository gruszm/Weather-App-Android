package com.example.weatherapp.openweatherapi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class WeatherResponse
{
    private List<WeatherData> list;
    private City city;

    private static class City
    {
        private String name;
        private Coord coord;
        private int timezone;
    }

    private static class Coord
    {
        private double lat;
        private double lon;
    }

    private static class WeatherData
    {
        private long dt;
        private Main main;
        private List<Weather> weather;
        private Wind wind;
        private int visibility;
    }

    private static class Wind
    {
        private double speed;
        private int deg;
    }

    private static class Main
    {
        private double temp;
        private int pressure;
        private int humidity;
    }

    private static class Weather
    {
        private String description;
    }

    public String getCityName()
    {
        return city.name;
    }


    public double getLatitude()
    {
        return city.coord.lat;
    }

    public double getLongitude()
    {
        return city.coord.lon;
    }


    public String getCurrentWeatherDescription()
    {
        return list.get(0).weather.get(0).description;
    }

    public String getWeatherDescription(int position)
    {
        return list.get(position).weather.get(0).description;
    }

    public int getCurrentPressure()
    {
        return list.get(0).main.pressure;
    }

    public int getPressure(int position)
    {
        return list.get(position).main.pressure;
    }

    public double getCurrentTemperature()
    {
        return list.get(0).main.temp;
    }

    public double getTemperature(int position)
    {
        return list.get(position).main.temp;
    }

    public String getCurrentTimestamp()
    {
        long timestamp = (list.get(0).dt + city.timezone) * 1000; // convert current timestamp to milliseconds
        Date date = new Date(timestamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        return simpleDateFormat.format(date);
    }

    public String getTimestamp(int position)
    {
        long timestamp = (list.get(position).dt + city.timezone) * 1000; // convert current timestamp to milliseconds
        Date date = new Date(timestamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        return simpleDateFormat.format(date);
    }

    public long getOldestDataTime()
    {
        return list.get(0).dt;
    }

    public double getCurrentWindSpeed()
    {
        return list.get(0).wind.speed;
    }

    public int getCurrentWindDirection()
    {
        return list.get(0).wind.deg;
    }

    public int getCurrentHumidity()
    {
        return list.get(0).main.humidity;
    }

    public int getCurrentVisibility()
    {
        return list.get(0).visibility;
    }
}
