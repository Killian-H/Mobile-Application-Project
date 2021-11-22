package edu.uw.tcss450.group7.chatapp.ui.weather;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class Weather_Main {
    private String url_weatherAPI;
    private String url_weatherAPI_Icon;
    private String weather_IconID;
    private JSONObject json_weather;
    private JSONObject json_timezone;
    private JSONObject json_weather_current;
    private JSONArray json_weather_hourlyList;
    private JSONObject json_weather_hourly;
    private JSONObject json_weather_daily;
    private JSONArray json_weather_dailyList;

    Weather_Main(double longitude, double latitude) throws MalformedURLException {
        getJsonData(longitude,latitude);

    }
    private void getJsonData(double longitude,double latitude) throws MalformedURLException {

        try {
            URL url_weatherAPI =new URL("https://mobile-application-project-450.herokuapp.com/weather");
            URL url_weatherAPI_Icon =new URL("https://mobile-application-project-450.herokuapp.com/weather");
        }
        catch (Exception e){ return;
        }
    }

}
