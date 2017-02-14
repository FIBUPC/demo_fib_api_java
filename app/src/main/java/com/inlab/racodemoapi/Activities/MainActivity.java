package com.inlab.racodemoapi.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.inlab.racodemoapi.Models.PlansEstudiModel;
import com.inlab.racodemoapi.R;
import com.inlab.racodemoapi.RetrofitSettings.Client;
import com.inlab.racodemoapi.RetrofitSettings.RacoAPIService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create a very simple REST adapter which points the GitHub API endpoint.
        RacoAPIService api = Client.getApiService();

// Fetch a list of the Github repositories.
        Call<List<PlansEstudiModel>> call =
                api.plansEstudi();

// Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<List<PlansEstudiModel>>() {
            @Override
            public void onResponse(Call<List<PlansEstudiModel>> call, Response<List<PlansEstudiModel>> response) {
                // The network call was a success and we got a response
                // TODO: use the repository list and display it
            }

            @Override
            public void onFailure(Call<List<PlansEstudiModel>> call, Throwable t) {
                // the network call was a failure
                // TODO: handle error
            }
        });
    }
}
