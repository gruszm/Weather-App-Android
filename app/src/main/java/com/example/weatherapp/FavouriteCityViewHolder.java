package com.example.weatherapp;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class FavouriteCityViewHolder extends RecyclerView.ViewHolder
{
    public TextView favouriteCityTV;
    private Button useCityBtn, deleteCityBtn;
    private FavouriteCityInterface favouriteCityInterface;

    public FavouriteCityViewHolder(View itemView, FavouriteCityInterface favouriteCityInterface)
    {
        super(itemView);

        favouriteCityTV = itemView.findViewById(R.id.favourite_cities_element_city_name_text_view);
        useCityBtn = itemView.findViewById(R.id.favourite_cities_element_use_button);
        deleteCityBtn = itemView.findViewById(R.id.favourite_cities_element_delete_button);

        useCityBtn.setOnClickListener(this::onFavouriteCityUseListener);
        deleteCityBtn.setOnClickListener(this::onFavouriteCityDeleteListener);

        this.favouriteCityInterface = favouriteCityInterface;
    }

    private void onFavouriteCityUseListener(View view)
    {
        favouriteCityInterface.useCity(favouriteCityTV.getText().toString());
    }

    private void onFavouriteCityDeleteListener(View view)
    {
        favouriteCityInterface.removeCity(getAdapterPosition());
    }
}
