package edu.uw.tcss450.group7.chatapp.utils;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import edu.uw.tcss450.group7.chatapp.R;

public class WeatherActivity extends AppCompatActivity {
    Location currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_weather);

    }
    public class weatherStuff extends AsyncTask{


        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }

    }
}
