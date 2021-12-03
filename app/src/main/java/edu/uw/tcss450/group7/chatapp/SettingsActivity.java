package edu.uw.tcss450.group7.chatapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;

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