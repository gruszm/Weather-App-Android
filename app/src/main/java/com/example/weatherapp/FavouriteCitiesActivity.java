package com.example.weatherapp;

import static com.example.weatherapp.WeatherAppConfig.WEATHER_APP_SHARED_PREFS_NAME;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
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

        SharedPreferences sharedPreferences = getSharedPreferences(WEATHER_APP_SHARED_PREFS_NAME, MODE_PRIVATE);

        favouriteCitiesRecyclerView = findViewById(R.id.favourite_cities_recycler_view);
        addCityET = findViewById(R.id.favourite_cities_add_city_edit_text);
        addCityBtn = findViewById(R.id.favourite_cities_add_city_button);

        favouriteCitiesList = new ArrayList<>(sharedPreferences.getStringSet("favourite_cities", new HashSet<>()));
        favouriteCitiesAdapter = new FavouriteCitiesAdapter(favouriteCitiesList, this);
        favouriteCitiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favouriteCitiesRecyclerView.setAdapter(favouriteCitiesAdapter);

        addCityET.setOnEditorActionListener(this::addCityOnEditorActionListener);
        addCityBtn.setOnClickListener(this::addCityOnClickListener);
    }

    private boolean addCityOnEditorActionListener(TextView textView, int actionId, KeyEvent keyEvent)
    {
        // when clicking "Done", add a new city and close the keyboard

        if (actionId == EditorInfo.IME_ACTION_DONE)
        {
            addCityOnClickListener(null);
        }

        return false;
    }

    private void addCityOnClickListener(View view)
    {
        String tempCity = addCityET.getText().toString();

        if (!favouriteCitiesList.contains(tempCity))
        {
            if (!tempCity.isEmpty())
            {
                addCityET.setText("");
                favouriteCitiesList.add(tempCity);
                favouriteCitiesAdapter.notifyItemInserted(favouriteCitiesList.size() - 1);

                SharedPreferences sharedPreferences = getSharedPreferences(WEATHER_APP_SHARED_PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putStringSet("favourite_cities", new HashSet<>(favouriteCitiesList));
                editor.apply();
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
        // in this usage of this interface, just update the list stored in the memory of the system
        SharedPreferences sharedPreferences = getSharedPreferences(WEATHER_APP_SHARED_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("favourite_cities", new HashSet<>(favouriteCitiesList));
        editor.apply();
    }
}
