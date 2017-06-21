package com.inlab.racodemoapi.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.inlab.racodemoapi.Constants.OAuthParams;
import com.inlab.racodemoapi.Models.User;
import com.inlab.racodemoapi.R;
import com.inlab.racodemoapi.ServiceSettings.AccessTokenService;
import com.inlab.racodemoapi.ServiceSettings.RacoAPIService;
import com.inlab.racodemoapi.ServiceSettings.ServiceGenerator;
import com.inlab.racodemoapi.ServiceSettings.TokenResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenuActivity extends AppCompatActivity {
    SharedPreferences prefs;
    String accessToken;
    Button signOutButton;
    RacoAPIService racoAPIService;
    TokenResponse ts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        signOutButton = (Button) findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPrefs();
                goToLogin();
            }
        });
        prefs = this.getSharedPreferences("com.inlab.racodemoapi", Context.MODE_PRIVATE);
        getUserInfo();
    }

    private void refreshAccessToken() {
        AccessTokenService ats = ServiceGenerator.createService(AccessTokenService.class);
        String refreshToken = prefs.getString("refreshToken", null);
        Call<TokenResponse> call = ats.getRefreshToken("refresh_token", refreshToken, OAuthParams.clientID, OAuthParams.clientSecret);
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful()) {
                    ts = response.body();
                    // Save the new values
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("accessToken", ts.getAccessToken());
                    System.out.println(ts.getAccessToken());
                    editor.putString("refreshToken", ts.getRefreshToken());
                    editor.apply();
                    // At this point, we go back to the call to get the user's info that will be displayed
                    getUserInfo();
                }
                else {
                    showErrorDialog(response.code());
                }

            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });


    }

    private void showErrorDialog(int code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Codi d'error: "+ code)
                .setTitle(R.string.dialog_error_title);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                clearPrefs();
                goToLogin();
            }
        });
        builder.create();
        builder.show();
    }

    private void getUserPhoto() {
        accessToken = prefs.getString("accessToken", null);
        racoAPIService = ServiceGenerator.createService(RacoAPIService.class, OAuthParams.clientID, OAuthParams.clientSecret, accessToken, this);
        Call<ResponseBody> call2 = racoAPIService.getMyPhoto();
        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
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
    }

    private void getUserInfo() {

        accessToken = prefs.getString("accessToken", null);
        racoAPIService = ServiceGenerator.createService(RacoAPIService.class, OAuthParams.clientID, OAuthParams.clientSecret, accessToken, this);
        Call<User> call1 = racoAPIService.getMyInfo();
        final TextView textViewJo = (TextView) findViewById(R.id.textViewJo);
        final TextView textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        final TextView textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        final TextView textViewAccessToken = (TextView) findViewById(R.id.textViewAccessToken);
        final TextView textViewRefreshToken = (TextView) findViewById(R.id.textViewRefreshToken);
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
                    String textAccessToken = "Access Token: " + accessToken;
                    textViewAccessToken.setText(textAccessToken);
                    String textRefreshToken = "Refresh Token: " + prefs.getString("refreshToken", null);
                    textViewRefreshToken.setText(textRefreshToken);

                }
                else {
                    if (response.code() == 401) {
                        // The call returns 401 if the access token has expired
                        refreshAccessToken();
                    }
                    else {
                        showErrorDialog(response.code());
                    }
                }

                getUserPhoto();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    private Activity getActivity() {
        return this;
    }

    private void goToLogin() {
        Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void clearPrefs() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("accessToken", null);
        editor.putString("refreshToken", null);
        editor.putBoolean("isLogged", false);
        editor.apply();
    }
}

