package com.example.weatherapp;

import static com.example.weatherapp.ForecastAdapter.FORECAST_NUM;
import static com.example.weatherapp.WeatherAppConfig.WEATHER_APP_SHARED_PREFS_NAME;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.weatherapp.openweatherapi.WeatherApiHandler;
import com.example.weatherapp.openweatherapi.WeatherResponse;
import com.example.weatherapp.openweatherapi.WeatherResponseCallback;

public class BasicInfoFragment extends Fragment implements AdapterView.OnItemSelectedListener
{
    private EditText cityET;
    private TextView cityNameTV, latitudeTV, longitudeTV, timeTV, pressureTv, temperatureTV, descriptionTV;
    private WeatherResponseCallback weatherResponseCallback;
    private WeatherResponse weatherHistoryInfo;
    private Spinner unitsSpinner;
    private ImageView weatherIcon;
    private String temperatureSuffix;
    private Button favouriteButton;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    public BasicInfoFragment(WeatherResponse weatherHistoryInfo, String temperatureSuffix, WeatherResponseCallback weatherResponseCallback)
    {
        this.weatherHistoryInfo = weatherHistoryInfo;
        this.temperatureSuffix = temperatureSuffix;
        this.weatherResponseCallback = weatherResponseCallback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result ->
                {
                    if (result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        String selectedCity = data.getStringExtra("selected_city");
                        cityET.setText(selectedCity);

                        // create an artificial text view and pass it to the method, which handles the OK button in the EditText
                        TextView artificialTextView = new TextView(getContext());
                        artificialTextView.setText(selectedCity);
                        onCityEditorAction(artificialTextView, EditorInfo.IME_ACTION_DONE, null);
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.basic_info_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        cityET = view.findViewById(R.id.city_name_edit_text);
        cityNameTV = view.findViewById(R.id.city_name_text_api_editable);
        latitudeTV = view.findViewById(R.id.latitude_text_editable);
        longitudeTV = view.findViewById(R.id.longitude_text_editable);
        timeTV = view.findViewById(R.id.time_text_editable);
        pressureTv = view.findViewById(R.id.pressure_text_editable);
        temperatureTV = view.findViewById(R.id.temperature_text_editable);
        descriptionTV = view.findViewById(R.id.description_text_editable);
        weatherIcon = view.findViewById(R.id.basic_info_weather_icon);
        favouriteButton = view.findViewById(R.id.basic_info_favourite_button);

        cityET.setOnEditorActionListener(this::onCityEditorAction);
        favouriteButton.setOnClickListener(this::favouriteButtonOnClickListener);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(WEATHER_APP_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        cityET.setText(sharedPreferences.getString("current_city", ""));

        if (weatherHistoryInfo != null)
        {
            updateWeatherInfo(weatherHistoryInfo, temperatureSuffix);
        }

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.units_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String spinnerSelectedItem = sharedPreferences.getString("current_units", null);

        unitsSpinner = view.findViewById(R.id.unit_spinner);
        unitsSpinner.setAdapter(spinnerAdapter);

        // important thing is, that the method setSelection is invoked before setting onItemSelectedListener
        if (spinnerSelectedItem != null)
        {
            int spinnerPos = spinnerAdapter.getPosition(spinnerSelectedItem);
            unitsSpinner.setSelection(spinnerPos, false);
        }

        unitsSpinner.setOnItemSelectedListener(this);
    }

    private void favouriteButtonOnClickListener(View view)
    {
        Intent intent = new Intent(getContext(), FavouriteCitiesActivity.class);
        activityResultLauncher.launch(intent);
    }

    boolean onCityEditorAction(TextView textView, int actionId, KeyEvent keyEvent)
    {
        if (actionId == EditorInfo.IME_ACTION_DONE)
        {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(WEATHER_APP_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("current_city", textView.getText().toString());
            editor.apply();

            WeatherApiHandler.getWeatherResponseFromApi(textView.getText().toString(), FORECAST_NUM, weatherResponseCallback, getContext());
        }

        return false;
    }

    public void updateWeatherInfo(WeatherResponse weatherResponse, String temperatureSuffix)
    {
        String temperature = String.valueOf(weatherResponse.getCurrentTemperature()).concat(temperatureSuffix);
        String iconName = "ic_".concat(weatherResponse.getIconId(0));
        String packageName = getContext().getPackageName();

        cityNameTV.setText(weatherResponse.getCityName());
        latitudeTV.setText(String.valueOf(weatherResponse.getLatitude()).concat("°"));
        longitudeTV.setText(String.valueOf(weatherResponse.getLongitude()).concat("°"));
        timeTV.setText(weatherResponse.getCurrentTimestamp());
        temperatureTV.setText(temperature);
        pressureTv.setText(String.valueOf(weatherResponse.getCurrentPressure()).concat(" hPa"));
        descriptionTV.setText(weatherResponse.getCurrentWeatherDescription());
        weatherIcon.setImageResource(getResources().getIdentifier(iconName, "drawable", packageName));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(WEATHER_APP_SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("current_units", parent.getSelectedItem().toString());
        editor.commit(); // commit() instead of apply() because we want to be sure, that the weather request uses updated units

        WeatherApiHandler.getWeatherResponseFromApi(cityET.getText().toString(), FORECAST_NUM, weatherResponseCallback, getContext());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {

    }
}
