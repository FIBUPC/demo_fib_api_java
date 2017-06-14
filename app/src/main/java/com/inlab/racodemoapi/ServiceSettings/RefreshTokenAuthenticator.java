package com.inlab.racodemoapi.ServiceSettings;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.inlab.racodemoapi.Activities.LoginActivity;
import com.inlab.racodemoapi.Constants.OAuthParams;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by florencia.rimolo on 09/06/2017.
 */

public class RefreshTokenAuthenticator implements Authenticator {
    private Context c;
    SharedPreferences prefs;
    TokenResponse ts;
    boolean respSuccessful = false;
    public RefreshTokenAuthenticator(Context c) {
        this.c = c;
    }
    @Override
    public Request authenticate(Route route, Response response) throws IOException {

        //Refresh Your Access Token Here
        prefs = c.getSharedPreferences("com.inlab.racodemoapi", Context.MODE_PRIVATE);
        String refreshToken = prefs.getString("refreshToken", null);
        //System.out.println(refreshToken);
        AccessTokenService ats = ServiceGenerator.createService(AccessTokenService.class);
        Call<TokenResponse> call = ats.getRefreshToken("refresh_token", refreshToken, OAuthParams.clientID, OAuthParams.clientSecret);
        retrofit2.Response<TokenResponse> tokenResponse = call.execute();
        if (tokenResponse.isSuccessful()) {
            ts = tokenResponse.body();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("accessToken", ts.getAccessToken());
            System.out.println(ts.getAccessToken());
            editor.putString("refreshToken", ts.getRefreshToken());
            editor.apply();
            return response.request().newBuilder()
                    .header("Authorization", "Bearer "+ts.getAccessToken())
                    .build();
        }
        else {
            return null;
        }
        /*
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, retrofit2.Response<TokenResponse> resp) {
                if (resp.isSuccessful()) {
                    ts = resp.body();
                    respSuccessful = true;
                }
                System.out.println("refresh "+resp.code());
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });

        if (respSuccessful) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("accessToken", ts.getAccessToken());
            System.out.println(ts.getAccessToken());
            editor.putString("refreshToken", ts.getRefreshToken());
            editor.apply();
            return response.request().newBuilder()
                    .header("Authorization", "Bearer "+ts.getAccessToken())
                    .build();
        }
        return null;
*/
    }

}
