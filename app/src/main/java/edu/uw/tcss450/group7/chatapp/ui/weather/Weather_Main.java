package edu.uw.tcss450.group7.chatapp.ui.weather;

import android.media.Image;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Weather_Main {
    private String myTimezone;
    private Double myLatitude;
    private Double myLongitude;
    private Weather_Current myCurrentWeather;
    private Weather_Hourly myHourlyForecast;
    private Weather_7Day my7DayForecast;

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
    public String getMyTimezone() {
        return myTimezone;
    }

    public Double getMyLatitude() {
        return myLatitude;
    }

    public Double getMyLongitude() {
        return myLongitude;
    }

    public Weather_Current getMyCurrentWeather() {
        return myCurrentWeather;
    }

    public Weather_Hourly getMyHourlyForecast() {
        return myHourlyForecast;
    }

    public Weather_7Day getMy7DayForecast() {
        return my7DayForecast;
    }




}
