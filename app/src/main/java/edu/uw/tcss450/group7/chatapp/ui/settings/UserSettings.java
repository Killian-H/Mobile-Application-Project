package edu.uw.tcss450.group7.chatapp.ui.settings;

import android.app.Application;

public class UserSettings extends Application {

    public static final String M_PREFERENCES = "preferences";
    public static final String M_CUSTOM_THEME = "customTheme";
    public static final String M_UW_THEME = "uwTheme";
    public static final String M_DARK_THEME = "darkTheme";

    private String customTheme;

    public String getCustomTheme() {
        return customTheme;
    }

    public void setCustomTheme(String customTheme) {
        this.customTheme = customTheme;
    }

}
