package edu.uw.tcss450.group7.chatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import edu.uw.tcss450.group7.chatapp.ui.settings.UserSettings;

public class AuthActivity extends AppCompatActivity {

    private View mAuthView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
    }

}
