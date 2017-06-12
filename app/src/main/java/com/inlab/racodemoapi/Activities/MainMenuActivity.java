package com.inlab.racodemoapi.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.inlab.racodemoapi.Constants.OAuthParams;
import com.inlab.racodemoapi.Models.User;
import com.inlab.racodemoapi.R;
import com.inlab.racodemoapi.ServiceSettings.RacoAPIService;
import com.inlab.racodemoapi.ServiceSettings.ServiceGenerator;

import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenuActivity extends AppCompatActivity {
    SharedPreferences prefs;
    String accessToken;
    Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        prefs = this.getSharedPreferences("com.inlab.racodemoapi", Context.MODE_PRIVATE);
        accessToken = prefs.getString("accessToken", null);
        signOutButton = (Button) findViewById(R.id.signOutButton);
        final RacoAPIService racoAPIService = ServiceGenerator.createService(RacoAPIService.class, OAuthParams.clientID, OAuthParams.clientSecret, accessToken);
        Call<User> call1 = racoAPIService.getMyInfo();
        final TextView textViewJo = (TextView) findViewById(R.id.textViewJo);
        final TextView textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        final TextView textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        call1.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    String jo = response.body().getNom();
                    jo += " " + response.body().getCognoms();
                    String username = response.body().getUsername();
                    String email = response.body().getEmail();
                    textViewJo.setText(jo);
                    textViewUsername.setText(username);
                    textViewEmail.setText(email);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
        Call<ResponseBody> call2 = racoAPIService.getMyPhoto();
        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    ImageView imageView = (ImageView) findViewById(R.id.imageView);
                    Bitmap bm = BitmapFactory.decodeStream(response.body().byteStream());
                    imageView.setImageBitmap(bm);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("accessToken", null);
                editor.putString("refreshToken", null);
                editor.putBoolean("isLogged", false);
                editor.apply();
                Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }
}

