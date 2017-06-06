package com.inlab.racodemoapi.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.inlab.racodemoapi.Models.User;
import com.inlab.racodemoapi.R;
import com.inlab.racodemoapi.ServiceSettings.RacoAPIService;
import com.inlab.racodemoapi.ServiceSettings.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenuActivity extends AppCompatActivity {
    SharedPreferences prefs;
    String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        prefs = this.getSharedPreferences("com.inlab.racodemoapi", Context.MODE_PRIVATE);
        accessToken  = prefs.getString("accessToken", null);
        System.out.println(accessToken);
        RacoAPIService racoAPIService = ServiceGenerator.createService(RacoAPIService.class,LoginActivity.clientId,LoginActivity.clientSecret,accessToken);
        Call<User> call1 = racoAPIService.getMyInfo();
        final TextView textView = (TextView) findViewById(R.id.textView);
        call1.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                String json = new Gson().toJson(response.body());
                System.out.println(json);
                String jo = response.body().getNom();
                String welcome = "Welcome "+jo;
                textView.setText(welcome);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
