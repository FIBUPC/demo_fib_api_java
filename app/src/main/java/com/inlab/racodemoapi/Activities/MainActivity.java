package com.inlab.racodemoapi.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.inlab.racodemoapi.Models.User;
import com.inlab.racodemoapi.R;
import com.inlab.racodemoapi.RetrofitSettings.RacoAPIService;
import com.inlab.racodemoapi.RetrofitSettings.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RacoAPIService racoAPIService = ServiceGenerator.createService(RacoAPIService.class,LoginActivity.clientId,LoginActivity.clientSecret,LoginActivity.accessToken.getAccessToken());
        Call<User> call1 = racoAPIService.getMyInfo();
        final TextView textView = (TextView) findViewById(R.id.textView);
        call1.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
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
