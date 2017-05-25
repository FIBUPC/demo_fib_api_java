package com.inlab.racodemoapi.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.Uri;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.inlab.racodemoapi.Models.User;
import com.inlab.racodemoapi.R;
import com.inlab.racodemoapi.ServiceSettings.AccessToken;
import com.inlab.racodemoapi.ServiceSettings.LoginService;
import com.inlab.racodemoapi.ServiceSettings.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    // you should either define client id and secret as constants or in string resources
    public static final String clientId = "o0OQumjT6MfVQsry7TFE2WnGD6wtFifxXDBlphei";
    public static final String clientSecret = "Uvs2ukTaoTgLj6j3NSUiMEt3YhdbTqhx5ETCLpkR3hT4JuzqM0y5EBzk0QQhjnj89PpTZtxFEFpn5v3uv5GDUz4UNdgYqf1nIwzYt65KpyaPOsrRJQRyHc54viBmiZLJ";
    private final String redirectUri = "apifib://raco";
    private final String responseType = "code";
    private AccessToken accessToken;
    SharedPreferences prefs;
    TextView textView;
    private User user;
    private boolean isLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textView = (TextView)findViewById(R.id.textViewJo);
        Button loginButton = (Button) findViewById(R.id.loginbutton);
        prefs = this.getSharedPreferences("com.inlab.racodemoapi",Context.MODE_PRIVATE);
        if (prefs.getBoolean("isLogged", false)) {
            goToMain();
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(ServiceGenerator.API_BASE_URL + "o/authorize/" + "?client_id=" + clientId + "&response_type=" + responseType + "&state=random_state_string"));
                startActivity(intent);
            }
        });
    }

    @Override
    // Catch the Authorization Code
    protected void onResume() {
        super.onResume();

        // the intent filter defined in AndroidManifest will handle the return from ACTION_VIEW intent
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(redirectUri)) {
            // use the parameter your API exposes for the code (mostly it's "code")
            String code = uri.getQueryParameter("code");
            if (code != null) {
                // At this point, we have the Authorization code, so we can get the Access token
                LoginService loginService =
                        ServiceGenerator.createService(LoginService.class);
                Call<AccessToken> call = loginService.getAccessToken("authorization_code",code, redirectUri,clientId, clientSecret);
                    call.enqueue(new Callback<AccessToken>() {
                        @Override
                        public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                            if (response.isSuccessful()) {
                                accessToken = response.body();
                                isLogged = true;
                                savePrefs();
                                goToMain();
                            }
                        }

                        @Override
                        public void onFailure(Call<AccessToken> call, Throwable t) {
                        }
                    });

            } else if (uri.getQueryParameter("error") != null) {
                // show an error message here
            }
        }
    }
    private void savePrefs() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("accessToken", this.accessToken.getAccessToken());
        editor.putBoolean("isLogged", this.isLogged);
        editor.apply();
    }
    public Activity getActivity() {
        return this;
    }

    public void goToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}

