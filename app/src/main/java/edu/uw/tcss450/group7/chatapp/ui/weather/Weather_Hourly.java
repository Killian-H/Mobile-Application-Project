package edu.uw.tcss450.group7.chatapp.ui.weather;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather_Hourly {
    private Object[] myHourlyWeatherArray;
    private int myHourRange;

    Weather_Hourly(JSONArray theJsonArray) throws JSONException {
        myHourRange = 24;
        fillArray(theJsonArray);

    }
    /**
    fills the Array constrained by hour range set in the constructor
     */
    private void fillArray(JSONArray theJSONArray) throws JSONException {
        myHourlyWeatherArray = new Object[myHourRange];
        for(int i = 0; i <= myHourRange;i++){
            try {
                Weather_Current temp = new Weather_Current((JSONObject) theJSONArray.get(i));
                myHourlyWeatherArray[i] = temp;
            }
            catch (JSONException e){
                Log.e("JSON PARSE", "JSON Parse Error in weatherHourly fillArray");
            }
        }
    }

    public Weather_Current getWeatherAtSpecificHour(int theHour){
        return (Weather_Current) myHourlyWeatherArray[theHour];
    }

    public Object[] getMyHourlyWeatherArray() {
        return myHourlyWeatherArray;
    }
}
