package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavouriteCitiesAdapter extends RecyclerView.Adapter<FavouriteCityViewHolder> implements FavouriteCityInterface
{
    private List<String> favouriteCitiesList;

    public FavouriteCitiesAdapter(List<String> favouriteCitiesList)
    {
        this.favouriteCitiesList = favouriteCitiesList;
    }

    @Override
    public FavouriteCityViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_cities_element, parent, false);

        return new FavouriteCityViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(FavouriteCityViewHolder holder, int position)
    {
        holder.favouriteCityTV.setText(favouriteCitiesList.get(position));
    }

    @Override
    public int getItemCount()
    {
        return favouriteCitiesList.size();
    }

    @Override
    public void useCity(String cityName)
    {

    }

    @Override
    public void removeCity(int position)
    {
        if (position >= 0)
        {
            favouriteCitiesList.remove(position);
            notifyItemRemoved(position);
        }
    }
}
