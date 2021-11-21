package edu.uw.tcss450.group7.chatapp.ui.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather_7Day extends Weather_Parent{
    private double myTempDay,MyTempNight,MyTempMin,MyTempMax,MyTempEve,MyTempMorn;
    private double myFeelsDay,MyFeelsNight,MyFeelsEve,MyFeelsMorn;

    Weather_7Day(JSONObject theJson) throws JSONException {
        super(theJson);
    }
}
