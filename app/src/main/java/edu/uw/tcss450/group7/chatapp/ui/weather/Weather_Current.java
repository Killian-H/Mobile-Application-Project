package edu.uw.tcss450.group7.chatapp.ui.weather;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;

public class Weather_Current {
    private Long myTime;
    private int myTimezoneOffset;
    private double myTemp;
    private Long mySunrise;
    private Long mySunset;
    private double myFeels;
    private double myPressure;
    private double myHumidity;
    private double myWindSpeed;
    private double myWindGust;
    private double myID;
    private String myIconID;
    private String myShortDescription;
    private String myLongDescription;


    Weather_Current(JSONObject theJson) throws JSONException {
        try{
            myTime= theJson.getLong("dt");
            if (theJson.get("temp") instanceof  JSONObject)myTemp = ((JSONObject) theJson.get("temp")).getDouble("day");
            else myTemp = theJson.getDouble("temp");
            mySunrise=theJson.getLong("sunrise");
            mySunset= theJson.getLong("sunset");
            if (theJson.get("feels_like") instanceof  JSONObject)myTemp = ((JSONObject) theJson.get("feels_like")).getDouble("day");
            else myFeels = theJson.getDouble("feels_like");
            myPressure = theJson.getDouble("pressure");
            myHumidity = theJson.getDouble("humidity");
            myWindSpeed = theJson.getDouble("wind_speed");
            myWindGust = theJson.getDouble("wind_gust");
            JSONObject weatherJson = (JSONObject) theJson.getJSONArray("weather").get(0);
            myID = weatherJson.getInt("id");
            myIconID = weatherJson.getString("icon");
            myShortDescription =  weatherJson.getString("main");
            myLongDescription = weatherJson.getString("description");
        }
        catch (JSONException e){
            Log.e("JSON PARSE", "JSON Parse Error in weatherCurrent");
        }

    }
    public String getMyTime(){
        return timeHelper(myTime);
    }
    public int getMyTimezoneOffset(){return myTimezoneOffset;}
    public double getMyTemp(){
        return myTemp;
    }
    public String getMySunrise(){
        return  timeHelper(mySunrise);
    }
    public  String getMySunset(){
        return timeHelper(mySunset);
    }
    public double getMyFeels(){
        return myFeels;
    }
    public double getMyPressure(){
        return myPressure;
    }
    public double getMyID() {
        return myID;
    }
    public String getMyIconID() {
        return myIconID;
    }
    public String getMyShortDescription() {
        return myShortDescription;
    }
    public String getMyLongDescription() {
        return myLongDescription;
    }
    public double getMyHumidity(){
        return  myHumidity;
    }
    public double getMyWindSpeed(){
        return myWindSpeed;
    }
    public double getMyWindGust() {
        return myWindGust;
    }

    private String timeHelper(Long theUTC){
        String timeFormatted = "unformatted";
        try {
            Date itemDate = new Date(theUTC * 1000L);
            timeFormatted = new SimpleDateFormat("EEE, d MMM yyyy HH:mm").format(itemDate);
        }
        catch (Exception e){
            Log.e("timeFormat","error in timeHelperMethod in weather_current");
        }
        return timeFormatted;
    }
}
