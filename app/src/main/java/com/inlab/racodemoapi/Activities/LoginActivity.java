package com.inlab.racodemoapi.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.inlab.racodemoapi.Constants.OAuthParams;
import com.inlab.racodemoapi.Models.User;
import com.inlab.racodemoapi.R;
import com.inlab.racodemoapi.ServiceSettings.AccessToken;
import com.inlab.racodemoapi.ServiceSettings.AccessTokenService;
import com.inlab.racodemoapi.ServiceSettings.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    private AccessToken accessToken;
    private SharedPreferences prefs;
    TextView textView;
    private boolean isLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textView = (TextView)findViewById(R.id.textViewJo);
        Button loginButton = (Button) findViewById(R.id.loginbutton);
        prefs = this.getSharedPreferences("com.inlab.racodemoapi",Context.MODE_PRIVATE);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If we click on login button, we'll be redirected to the REDIRECT_URI parameter of our application
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(ServiceGenerator.API_BASE_URL + "o/authorize/" + "?client_id=" + OAuthParams.clientID + "&response_type=" + OAuthParams.responseType + "&state=random_state_string"));
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
        if (uri != null && uri.toString().startsWith(OAuthParams.redirectUri)) {
            // use the parameter your API exposes for the code (mostly it's "code")
            String code = uri.getQueryParameter("code");
            if (code != null) {
                // At this point, we have the Authorization code, so we can get the Access token
                AccessTokenService accessTokenService =
                        ServiceGenerator.createService(AccessTokenService.class);
                Call<AccessToken> call = accessTokenService.getAccessToken("authorization_code",code, OAuthParams.redirectUri,OAuthParams.clientID, OAuthParams.clientSecret);
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
                            Log.d("onFailure", t.toString());
                        }
                    });

            } else if (uri.getQueryParameter("error") != null) {
                // show an error message
                Log.d("onFailure", uri.getQueryParameter("error"));
            }
        }
    }
    private void savePrefs() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("accessToken", this.accessToken.getAccessToken());
        editor.putBoolean("isLogged", this.isLogged);
        editor.apply();
    }

    public void goToMain() {
        Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}

