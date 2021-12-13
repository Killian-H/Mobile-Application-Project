package edu.uw.tcss450.group7.chatapp.ui.weather;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Main weather object for all weather objects to be built based on
 * @version: 12/12/2021
 * @author Aaron Purslow
 * Commented by: Aaron Purslow
 */
public class Weather_Current {
    //my Time which includes the date to be returned by the JSON response
    private Double myTime;
    //my temp to be returned by the JSON response
    private double myTemp;
    //my Sunrise to be returned by the JSON response
    private Double mySunrise;
    //my Sunset to be returned by the JSON response
    private Double mySunset;
    //my feels like temp to be returned by the JSON response
    private double myFeels;
    //my pressure to be returned by the JSON response
    private double myPressure;
    //my humidity percentage to be returned by the JSON response
    private double myHumidity;
    //my winddspeed to be returned by the JSON response
    private double myWindSpeed;
    //my ID to be returned by the JSON response
    private double myID;
    //my IconID to be used for filling in background images for weather objects to be returned by the JSON response
    private String myIconID;
    //my short description about the weather to be returned by the JSON response
    private String myShortDescription;
    //my long description about the weather to be returned by the JSON response
    private String myLongDescription;


    /**
     *Creates weather object based on weather data.
     * Note: weather data for current, 24 hour, and 7 day all are slightly different.
     * For instance 24hour weather data doesn't contain sunset/sunrise because it is only looking at the hour
     * @param theJson the Json returned from OpenWeather api
     * @throws JSONException
     */
    Weather_Current(JSONObject theJson) throws JSONException {
        try{
            myTime= theJson.getDouble("dt");
            if (theJson.get("temp") instanceof  JSONObject)myTemp = ((JSONObject) theJson.get("temp")).getDouble("day");
            else myTemp = theJson.getDouble("temp");

            if (theJson.get("feels_like") instanceof  JSONObject)myTemp = ((JSONObject) theJson.get("feels_like")).getDouble("day");
            else myFeels = theJson.getDouble("feels_like");
            myPressure = theJson.getDouble("pressure");
            myHumidity = theJson.getDouble("humidity");
            myWindSpeed = theJson.getDouble("wind_speed");

            JSONObject weatherJson = (JSONObject) theJson.getJSONArray("weather").get(0);
            myID = weatherJson.getInt("id");
            myIconID = weatherJson.getString("icon");
            myShortDescription =  weatherJson.getString("main");
            myLongDescription = weatherJson.getString("description");
            //Sunrise last to be constructed because hourly doesn't contain sunrise/sunset
            if ((theJson.has("sunrise")))mySunrise=theJson.getDouble("sunrise");
          // mySunrise=theJson.getDouble("sunrise");
          if (theJson.has("sunset"))mySunset= theJson.getDouble("sunset");
        }
        catch (JSONException e){
            Log.e("JSON PARSE", "JSON Parse Error in weatherCurrent");
        }

    }

    /**
     * @return formatted time
     */
    public String getMyTime(){
        return timeHelper(myTime);
    }

    /**
     * @return temperature
     */
    public double getMyTemp(){
        return myTemp;
    }

    /**
     * @return formatted sunrise time
     */
    public String getMySunrise(){
        return  timeHelper(mySunrise);
    }

    /**
     * @return formatted sunset time
     */
    public  String getMySunset(){
        return timeHelper(mySunset);
    }

    /**
     * @return feels like description
     */
    public double getMyFeels(){
        return myFeels;
    }

    /**
     * @return pressure
     */
    public double getMyPressure(){
        return myPressure;
    }

    /**
     * @return ID
     */
    public double getMyID() {
        return myID;
    }

    /**
     * @return Icon Id for current weather. see also Fragment_weather onViewCreated
     */
    public String getMyIconID() {
        return myIconID;
    }

    /**
     * @return short weather current description
     */
    public String getMyShortDescription() {
        return myShortDescription;
    }

    /**
     * @return long weather current description
     */
    public String getMyLongDescription() {
        return myLongDescription;
    }

    /**
     * @return humidity percentage
     */
    public double getMyHumidity(){
        return  myHumidity;
    }

    /**
     * @return wind speed
     */
    public double getMyWindSpeed(){
        return myWindSpeed;
    }

    /**
     * helper to convert UTC to readable time
     * @param theUTC the Json respones UTC time
     * @return formatted time
     */
    private String timeHelper(Double theUTC){
        String timeFormatted = "unformatted";
        try {
            Date itemDate = new Date((long) (theUTC * 1000L));
            timeFormatted = new SimpleDateFormat("EEE, d MMM yyyy HH:mm").format(itemDate);
        }
        catch (Exception e){
            Log.e("timeFormat","error in timeHelperMethod in weather_current");
        }
        return timeFormatted;
    }
}
