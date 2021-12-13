/*
 * TCSS 450
 *
 * Controls the theme of the application.
 */
package edu.uw.tcss450.group7.chatapp;

import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.uw.tcss450.group7.chatapp.R;

/**
 * Abstract Activity which the other activities extend to provide for simple
 * theme changing.
 */
public abstract class ColorActivity extends AppCompatActivity {

    /* String for the current theme. */
    private static final String M_KEY_THEME = "Theme";

    /* The black and purple theme ID number. */
    private static final int M_BLACK_YELLOW = R.style.AppTheme_BP;

    /* The UW theme ID number. */
    private static final int M_UW_THEME = R.style.AppTheme_UW;

    /* The current theme ID. */
    private static int mCurrentTheme = M_UW_THEME;

    /**
     * Gets the current prefered theme when created from shared preferences.
     *
     * @param savedInstanceState Stores the data needed to reload the state of the
     *                           UI controller for this fragment.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mCurrentTheme = PreferenceManager.getDefaultSharedPreferences(this).getInt(M_KEY_THEME, M_UW_THEME);
        super.onCreate(savedInstanceState);
    }

    /**
     * Sets the current theme.
     */
    public void setTheme() {
        setTheme(mCurrentTheme);
    }

    /**
     * Gets the current theme.
     *
     * @return The current theme.
     */
    public int getTheTheme() { return mCurrentTheme; }

    /**
     * Switches between the two possible themes.
     */
    public void switchTheme() {
        if (mCurrentTheme == M_UW_THEME) {
            mCurrentTheme = M_BLACK_YELLOW;
        } else if (mCurrentTheme == M_BLACK_YELLOW){
            mCurrentTheme = M_UW_THEME;
        }
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(M_KEY_THEME, mCurrentTheme).apply();
    }
}