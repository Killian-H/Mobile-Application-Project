package edu.uw.tcss450.group7.chatapp.ui.weather;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather_7Day  {

    private Object[] my7DayWeatherArray;


    Weather_7Day(JSONArray theJsonArray) throws JSONException {
        fillArray(theJsonArray);
    }
    /**
     fills the Array constrained by hour range set in the constructor
     */
    private void fillArray(JSONArray theJSONArray) throws JSONException {
        my7DayWeatherArray = new Object[theJSONArray.length()];
        for(int i = 0; i < theJSONArray.length();i++){
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

    public Object[] getMy7DayWeatherArray() {
        return my7DayWeatherArray;
    }
}
