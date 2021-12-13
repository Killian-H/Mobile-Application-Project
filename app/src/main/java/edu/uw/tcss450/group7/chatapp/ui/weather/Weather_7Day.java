package edu.uw.tcss450.group7.chatapp.ui.weather;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * 7 day weather forecast object that contains weather_current for each day
 * @version: 12/12/2021
 * @author Aaron Purslow
 * Commented by: Aaron Purslow
 */
public class Weather_7Day {
    //Stores all the weather objects for either 7 day forecast
    private Weather_Current[] my7DayWeatherArray;


    Weather_7Day(JSONArray theJsonArray) throws JSONException {
        fillArray(theJsonArray);
    }

    /**
     * fills an array with weather objects that displays each day or hour data
     * @param theJSONArray the Json Array for 7 day. See also Weather_main.
     * @throws JSONException
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

    /**
     * @return return the WeatherArray
     */
    public Weather_Current[] getMy7DayWeatherArray() {
        return my7DayWeatherArray;
    }
}
