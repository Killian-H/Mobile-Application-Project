package edu.uw.tcss450.group7.chatapp.ui.weather;

import android.media.Image;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class Weather_Main {
    private String myTimezone;
    private Double myLatitude;
    private Double myLongitude;
    private Weather_Current myCurrentWeather;
    private Weather_Hourly myHourlyForecast;
    private Weather_7Day my7DayForecast;


    Weather_Main(double longitude, double latitude) throws MalformedURLException {
        getJsonData(longitude,latitude);

    }
    private void getJsonData(double longitude,double latitude) throws MalformedURLException {

        try {
            URL url_weatherAPI =new URL("https://mobile-application-project-450.herokuapp.com/weather");
            URL url_weatherAPI_Icon =new URL("https://mobile-application-project-450.herokuapp.com/weather");
        }
        catch (Exception e){
            Log.e("URL Connection", "error in getJsonData");
            return;
        }
    }

}
