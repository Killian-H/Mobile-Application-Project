package edu.uw.tcss450.group7.chatapp.ui.weather;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather_Hourly {
    private JSONArray myHourlyJsonArray;
    Weather_Hourly(JSONArray theHourlyJsonArray){
        myHourlyJsonArray = theHourlyJsonArray;
    }
    public Weather_Parent getSpecificHourWeather(int theIndex) throws JSONException {
        Weather_Parent weatherObject = null;
        try {
            weatherObject=new Weather_Parent((JSONObject) myHourlyJsonArray.get(theIndex));
        }
        catch (JSONException e){
            Log.e("JSON PARSE", "JSON Parse Error in weatherHourly 19");
        }
        return weatherObject;
    }
}
