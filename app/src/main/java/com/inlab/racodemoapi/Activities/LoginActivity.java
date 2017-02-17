package com.inlab.racodemoapi.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.inlab.racodemoapi.Models.JoModel;
import com.inlab.racodemoapi.R;
import com.inlab.racodemoapi.RetrofitSettings.AccessToken;
import com.inlab.racodemoapi.RetrofitSettings.LoginService;
import com.inlab.racodemoapi.RetrofitSettings.RacoAPIService;
import com.inlab.racodemoapi.RetrofitSettings.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends Activity {

    // you should either define client id and secret as constants or in string resources
    private final String clientId = "o0OQumjT6MfVQsry7TFE2WnGD6wtFifxXDBlphei";
    private final String clientSecret = "Uvs2ukTaoTgLj6j3NSUiMEt3YhdbTqhx5ETCLpkR3hT4JuzqM0y5EBzk0QQhjnj89PpTZtxFEFpn5v3uv5GDUz4UNdgYqf1nIwzYt65KpyaPOsrRJQRyHc54viBmiZLJ";
    private final String redirectUri = "apifib://raco";
    private final String responseType = "code";
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textView = (TextView)findViewById(R.id.textViewJo);
        Button loginButton = (Button) findViewById(R.id.loginbutton);
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
                // get access token
                LoginService loginService =
                        ServiceGenerator.createService(LoginService.class);
                Call<AccessToken> call = loginService.getAccessToken("authorization_code",code, redirectUri,clientId, clientSecret);
                System.out.println(code);
                    call.enqueue(new Callback<AccessToken>() {
                        @Override
                        public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                            System.out.println("CODE="+response.code());
                            if (response.isSuccessful()) {
                                AccessToken accessToken = response.body();
                                String json = new Gson().toJson(accessToken);
                                System.out.println(json);
                                String success = "Log in successful";
                                textView.setText(success);
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
}

