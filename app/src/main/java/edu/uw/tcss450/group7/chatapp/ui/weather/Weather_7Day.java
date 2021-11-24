package edu.uw.tcss450.group7.chatapp.ui.weather;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather_7Day  {

    private Weather_Current[] my7DayWeatherArray;


    Weather_7Day(JSONArray theJsonArray) throws JSONException {
        fillArray(theJsonArray);
    }
    /**
     fills the Array constrained by hour range set in the constructor
     */
    private void fillArray(@NonNull JSONArray theJSONArray) throws JSONException {
        my7DayWeatherArray = new Weather_Current[theJSONArray.length() - 1];
        for(int i = 0; i < theJSONArray.length() - 1; i++){
            try {
                Weather_Current temp = new Weather_Current((JSONObject) theJSONArray.get(i));
                my7DayWeatherArray[i] = temp;
            }
            catch (JSONException e){
                Log.e("JSON PARSE", "JSON Parse Error in 7dayWeather fillArray");
            }
        }
    }
    public Weather_Current getWeatherAtSpecificDay(int theIndex){
        return (Weather_Current) my7DayWeatherArray[theIndex];
    }

    public Weather_Current[] getMy7DayWeatherArray() {
        return my7DayWeatherArray;
    }
}
