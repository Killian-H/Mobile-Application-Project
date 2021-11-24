package edu.uw.tcss450.group7.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import edu.uw.tcss450.group7.chatapp.MainActivity;
import edu.uw.tcss450.group7.chatapp.R;
import edu.uw.tcss450.group7.chatapp.ColorActivity;

public class SettingsActivity extends ColorActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_settings);
        Button btn = findViewById(R.id.changeThemeButton);
        btn.setOnClickListener(view -> {
            switchTheme();
            recreate();
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}