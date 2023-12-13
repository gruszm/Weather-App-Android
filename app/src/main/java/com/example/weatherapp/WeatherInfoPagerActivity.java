package com.example.weatherapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class WeatherInfoPagerActivity extends FragmentActivity
{
    private static final int NUM_PAGES = 5;

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

    private class WeatherInfoPagerAdapter extends FragmentStateAdapter
    {
        public WeatherInfoPagerAdapter(FragmentActivity fragmentActivity)
        {
            super(fragmentActivity);
        }

        @Override
        public Fragment createFragment(int position)
        {
            return new WeatherInfoFragment();
        }

        @Override
        public int getItemCount()
        {
            return NUM_PAGES;
        }
    }
}
