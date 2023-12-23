package com.example.weatherapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import java.util.Scanner;

public class WeatherInfoFragment extends Fragment
{
    private TextView temperatureTV;
    private Button increaseTemperatureBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.weather_info_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        temperatureTV = view.findViewById(R.id.temperatura_tv_editable);
        increaseTemperatureBtn = view.findViewById(R.id.button);

        increaseTemperatureBtn.setOnClickListener(this::increaseTemperatureListener);
    }

    private void increaseTemperatureListener(View view)
    {
        String temperature = temperatureTV.getText().toString();
        Scanner scanner = new Scanner(temperature);
        int temperatureAsInt = scanner.nextInt();
        temperatureAsInt++;
        String rest = "";

        while (scanner.hasNext())
        {
            rest += scanner.next() + " ";
        }

        temperatureTV.setText(temperatureAsInt + " " + rest);
        scanner.close();
    }
}
