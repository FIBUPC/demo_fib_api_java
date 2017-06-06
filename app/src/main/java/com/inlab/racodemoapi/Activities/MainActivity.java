package com.inlab.racodemoapi.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.inlab.racodemoapi.R;


public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = this.getSharedPreferences("com.inlab.racodemoapi", Context.MODE_PRIVATE);

        final Intent intent = nextActivity();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        }, 2000);
    }

    private Intent nextActivity() {
        if (! prefs.getBoolean("isLogged", false)) {
            return new Intent(MainActivity.this, MainMenuActivity.class);
        } else {
            return new Intent(MainActivity.this, LoginActivity.class);
        }
    }

}
