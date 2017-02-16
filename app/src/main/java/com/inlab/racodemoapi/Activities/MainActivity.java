package com.inlab.racodemoapi.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.gson.Gson;
import com.inlab.racodemoapi.R;
import com.inlab.racodemoapi.RetrofitSettings.ServiceGenerator;
import com.inlab.racodemoapi.RetrofitSettings.RacoAPIService;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);
        // Create a very simple REST adapter which points the GitHub API endpoint.
        RacoAPIService api = ServiceGenerator.getApiService();

// Fetch a list of the Github repositories.
        Call<PlansEstudiModel> call =
                api.plansEstudi();

// Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<PlansEstudiModel>() {
            @Override
            public void onResponse(Call<PlansEstudiModel> call, Response<PlansEstudiModel> response) {
                // The network call was a success and we got a response
                // TODO: use the repository list and display it
                PlansEstudiModel l = response.body();
                String json = new Gson().toJson(l);
                textView.setText(json);
            }

            @Override
            public void onFailure(Call<PlansEstudiModel> call, Throwable t) {
                // the network call was a failure
                // TODO: handle error
            }
        });
    }
}
