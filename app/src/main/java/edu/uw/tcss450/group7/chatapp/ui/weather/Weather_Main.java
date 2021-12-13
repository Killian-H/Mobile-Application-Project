package edu.uw.tcss450.group7.chatapp.ui.weather;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Main collection of weather objects, the complete Json response put into object form
 * @version: 12/12/2021
 * @author Aaron Purslow
 * Commented by: Aaron Purslow
 */
public class Weather_Main {
    //timezone description
    private String myTimezone;
    //latitude requested
    private Double myLatitude;
    //longitude requested
    private Double myLongitude;
    //Current weather section
    private Weather_Current myCurrentWeather;
    //Hourly forecast section
    private Weather_Hourly myHourlyForecast;
    //7day forecast section
    private Weather_7Day my7DayForecast;

    /**
     * Main receiver for the Json response sends Json objects to appropriate weather objects to be created and stored back here.
     * @param theJson the main json response
     */
    Weather_Main(JSONObject theJson){
        try {
            myLatitude = theJson.getDouble("lat");
            myLongitude = theJson.getDouble("lon");
            myTimezone= theJson.getString("timezone");
            myCurrentWeather = new Weather_Current(theJson.getJSONObject("current"));
            myHourlyForecast = new Weather_Hourly(theJson.getJSONArray("hourly"));
            my7DayForecast = new Weather_7Day(theJson.getJSONArray("daily"));
        }
        catch (JSONException e){
            Log.e("JSON PARSE", "JSON Parse Error in weatherMain");
            Log.e("JSON PARSE", theJson.toString());
        }

    }

    /**
     * @return timezone
     */
    public String getMyTimezone() {
        return myTimezone;
    }

    /**
     * @return latitude
     */
    public Double getMyLatitude() {
        return myLatitude;
    }

    /**
     * @return longitude
     */
    public Double getMyLongitude() {
        return myLongitude;
    }

    /**
     * @return current weather object
     */
    public Weather_Current getMyCurrentWeather() {
        return myCurrentWeather;
    }

    /**
     * @return hourly weather object
     */
    public Weather_Hourly getMyHourlyForecast() {
        return myHourlyForecast;
    }

    /**
     * @return 7 day weather object
     */
    public Weather_7Day getMy7DayForecast() {
        return my7DayForecast;
    }




}
