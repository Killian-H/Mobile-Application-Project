package edu.uw.tcss450.yavuzalp.lab1myfirstapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Lifecycle:","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Lifecycle:","onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("Lifecycle:","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("Lifecycle:","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Lifecycle:","onDestroy");
    }
}