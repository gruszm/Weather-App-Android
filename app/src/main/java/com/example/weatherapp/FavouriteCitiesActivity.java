package com.example.weatherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FavouriteCitiesActivity extends AppCompatActivity implements FavouriteCityInterface
{
    private List<String> favouriteCitiesList;
    private RecyclerView favouriteCitiesRecyclerView;
    private FavouriteCitiesAdapter favouriteCitiesAdapter;
    private EditText addCityET;
    private Button addCityBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_cities);

        favouriteCitiesRecyclerView = findViewById(R.id.favourite_cities_recycler_view);
        addCityET = findViewById(R.id.favourite_cities_add_city_edit_text);
        addCityBtn = findViewById(R.id.favourite_cities_add_city_button);

        favouriteCitiesList = new ArrayList<>();
        favouriteCitiesAdapter = new FavouriteCitiesAdapter(favouriteCitiesList, this);
        favouriteCitiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favouriteCitiesRecyclerView.setAdapter(favouriteCitiesAdapter);

        addCityBtn.setOnClickListener(this::addCityOnClickListener);
    }

    private void addCityOnClickListener(View view)
    {
        String tempCity = addCityET.getText().toString();

        if (!favouriteCitiesList.contains(tempCity))
        {
            if (!tempCity.isEmpty())
            {
                favouriteCitiesList.add(tempCity);
                favouriteCitiesAdapter.notifyItemInserted(favouriteCitiesList.size() - 1);
            }
        }
        else
        {
            Toast.makeText(this, "This city is already on the list", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void useCity(String cityName)
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("selected_city", cityName);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void removeCity(int position)
    {
        // do nothing
    }
}
