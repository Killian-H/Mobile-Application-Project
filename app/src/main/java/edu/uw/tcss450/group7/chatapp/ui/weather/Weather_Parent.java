package edu.uw.tcss450.group7.chatapp.ui.weather;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather_Parent {
    private double myTime;
    private double myTemp;
    private double mySunrise;
    private double mySunset;
    private double myFeels;
    private double myPressure;
    private double myHumidity;
    private double myWindSpeed;
    private double myWindGust;
    private double myID;
    private String myIconID;
    private String myShortDescription;
    private String myLongDescription;


    Weather_Parent(JSONObject theJson) throws JSONException {
        try{
            myTime= theJson.getInt("dt");
            myTemp = theJson.getDouble("temp");
            mySunrise=theJson.getInt("sunrise");
            mySunset= theJson.getInt("sunset");
            myFeels = theJson.getDouble("feels_like");
            myPressure = theJson.getDouble("pressure");
            myHumidity = theJson.getDouble("humidity");
            myWindSpeed = theJson.getDouble("wind_speed");
            myWindGust = theJson.getDouble("wind_gust");
            JSONObject weatherJson = (JSONObject) theJson.getJSONArray("weather").get(0);
            myID = weatherJson.getInt("id");
            myIconID = theJson.getString("icon");
            myShortDescription =  theJson.getString("main");
            myLongDescription = theJson.getString("description");

        }
        catch (JSONException e){
            Log.e("JSON PARSE", "JSON Parse Error in weatherParent 49");
        }

    }
    public double getMyTime(){
        return myTime;
    }
    public double getMyTemp(){
        return myTemp;
    }
    public double getMySunrise(){
        return  mySunrise;
    }
    public  double getMySunset(){
        return mySunset;
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
}
