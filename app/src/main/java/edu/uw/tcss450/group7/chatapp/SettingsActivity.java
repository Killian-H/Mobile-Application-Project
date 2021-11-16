package edu.uw.tcss450.group7.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import edu.uw.tcss450.group7.chatapp.ui.settings.UserSettings;

public class SettingsActivity extends AppCompatActivity {

    private View mSettingView;

    private SwitchMaterial mThemeSwitch;
    private TextView mTheme, mSettings;
    private UserSettings mUserSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mUserSettings = (UserSettings) getApplication();
        initWidgets();
        loadSharedPreferences();
        initSwitchListener();
    }

    private void initWidgets() {
        mTheme = findViewById(R.id.change_theme);
        mSettings = findViewById(R.id.title);
        mThemeSwitch = findViewById(R.id.theme_switch);
        mSettingView = findViewById(R.id.settingsView);
    }

    private void loadSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(mUserSettings.M_PREFERENCES, MODE_PRIVATE);
        String theme = sharedPreferences.getString(mUserSettings.M_CUSTOM_THEME, UserSettings.M_UW_THEME);
        mUserSettings.setCustomTheme(theme);
    }



    private void initSwitchListener() {
        mThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mUserSettings.setCustomTheme(UserSettings.M_DARK_THEME);
                } else {
                    mUserSettings.setCustomTheme(UserSettings.M_UW_THEME);
                }
                SharedPreferences.Editor editor = getSharedPreferences(UserSettings.M_PREFERENCES, MODE_PRIVATE).edit();
                editor.putString(UserSettings.M_CUSTOM_THEME, mUserSettings.getCustomTheme());
                editor.apply();
                updateView();
            }
        });
    }

    private void updateView() {
        final int white = ContextCompat.getColor(this, R.color.white);
        final int black = ContextCompat.getColor(this, R.color.black);
        final int uwLight = ContextCompat.getColor(this, R.color.primaryUWLightColor);
        final int uwDark = ContextCompat.getColor(this, R.color.primaryUWDarkColor);
        final int uwPrim = ContextCompat.getColor(this, R.color.primaryUWColor);
        final int uwBackground = ContextCompat.getColor(this, R.color.primaryColor);
        final int uwSecColor = ContextCompat.getColor(this, R.color.secondaryUWColor);
        final int byDark = ContextCompat.getColor(this, R.color.primaryDarkColor);
        final int byPrim = ContextCompat.getColor(this, R.color.primaryColor);
        final int bySecColor = ContextCompat.getColor(this, R.color.secondaryColor);
        final int bySecLightColor = ContextCompat.getColor(this, R.color.secondaryLightColor);
        final int bySecDarkColor = ContextCompat.getColor(this, R.color.secondaryDarkColor);
        if(mUserSettings.getCustomTheme().equals(mUserSettings.M_DARK_THEME)) {

            mSettings.setTextColor(white);
            mThemeSwitch.setTextColor(white);
            mSettingView.setBackgroundColor(byPrim);
            mThemeSwitch.setChecked(true);
        } else {
            mSettings.setTextColor(black);
            mThemeSwitch.setTextColor(black);
            mSettingView.setBackgroundColor(white);
            mThemeSwitch.setChecked(false);
        }
    }
}