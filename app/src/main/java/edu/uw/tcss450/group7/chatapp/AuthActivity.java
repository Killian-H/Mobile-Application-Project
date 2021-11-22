package edu.uw.tcss450.group7.chatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.tcss450.group7.chatapp.model.PushyTokenViewModel;
import edu.uw.tcss450.group7.chatapp.ui.settings.UserSettings;
import me.pushy.sdk.Pushy;

public class AuthActivity extends AppCompatActivity {

    private View mAuthView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Pushy.listen(this);
        initiatePushyTokenRequest();
    }


    private void initiatePushyTokenRequest() {
        new ViewModelProvider(this).get(PushyTokenViewModel.class).retrieveToken(); }

}
