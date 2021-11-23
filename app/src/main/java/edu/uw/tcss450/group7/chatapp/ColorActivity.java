package edu.uw.tcss450.group7.chatapp;

import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.uw.tcss450.group7.chatapp.R;

public abstract class ColorActivity extends AppCompatActivity {
    private static final String M_KEY_THEME = "Theme";
    private static final int M_BLACK_YELLOW = R.style.AppTheme_BP;
    private static final int M_UW_THEME = R.style.AppTheme_UW;
    private static int mCurrentTheme = M_UW_THEME;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mCurrentTheme = PreferenceManager.getDefaultSharedPreferences(this).getInt(M_KEY_THEME, M_UW_THEME);
        super.onCreate(savedInstanceState);
    }

    public void setTheme() {
        setTheme(mCurrentTheme);
    }

    public int getTheTheme() { return mCurrentTheme; }

    public void switchTheme() {
        if (mCurrentTheme == M_UW_THEME) {
            mCurrentTheme = M_BLACK_YELLOW;
        } else if (mCurrentTheme == M_BLACK_YELLOW){
            mCurrentTheme = M_UW_THEME;
        }
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(M_KEY_THEME, mCurrentTheme).apply();
    }
}