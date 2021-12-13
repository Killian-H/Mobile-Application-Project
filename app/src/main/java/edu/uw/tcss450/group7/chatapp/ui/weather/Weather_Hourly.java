package edu.uw.tcss450.group7.chatapp.ui.weather;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * hourly weather forecast object that contains weather_current for each hour
 * @version: 12/12/2021
 * @author Aaron Purslow
 * Commented by: Aaron Purslow
 */
public class Weather_Hourly {
    //Hourly weather object array
    private Weather_Current[] myHourlyWeatherArray;
    //Json returns 48 hours worth of data as stated by the openweather api. Here we restrict it to 24 hour.
    private int myHourRange;

    Weather_Hourly(JSONArray theJsonArray) throws JSONException {
        myHourRange = 24;
        fillArray(theJsonArray);

    }

    /**
     * fills the weather array with weather currents that correlate with each hour forecasts
     * @param theJSONArray Json Array for hourly
     * @throws JSONException
     */
    private void fillArray(JSONArray theJSONArray) throws JSONException {
        myHourlyWeatherArray = new Weather_Current[myHourRange];
        for(int i = 0; i < myHourRange;i++){
            try {
                Weather_Current temp = new Weather_Current((JSONObject) theJSONArray.get(i));
                myHourlyWeatherArray[i] = temp;
            }
            catch (JSONException e){
                Log.e("JSON PARSE", "JSON Parse Error in weatherHourly fillArray");
            }
        }
    }

    /**
     * @param theHour the specific hour
     * @return the specific weather_current at an hour
     */
    public Weather_Current getWeatherAtSpecificHour(int theHour){
        return (Weather_Current) myHourlyWeatherArray[theHour];
    }

    /**
     * @return Hourly weather array
     */
    public Weather_Current[] getMyHourlyWeatherArray() {
        return myHourlyWeatherArray;
    }
}
