package com.example.weatherapp;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ForecastElementViewHolder extends RecyclerView.ViewHolder
{
    public TextView timeTV, temperatureTV, pressureTV, descTV;

    public ForecastElementViewHolder(View itemView)
    {
        super(itemView);

        timeTV = itemView.findViewById(R.id.forecast_element_time_text_view_editable);
        temperatureTV = itemView.findViewById(R.id.forecast_element_temperature_text_view_editable);
        pressureTV = itemView.findViewById(R.id.forecast_element_pressure_text_view_editable);
        descTV = itemView.findViewById(R.id.forecast_element_desc_text_view_editable);
    }
}
