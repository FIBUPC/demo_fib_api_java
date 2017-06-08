package com.inlab.racodemoapi.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        prefs = this.getSharedPreferences("com.inlab.racodemoapi", Context.MODE_PRIVATE);
        accessToken = prefs.getString("accessToken", null);
        System.out.println(accessToken);
        final RacoAPIService racoAPIService = ServiceGenerator.createService(RacoAPIService.class, LoginActivity.clientId, LoginActivity.clientSecret, accessToken);
        Call<User> call1 = racoAPIService.getMyInfo();
        final TextView textViewJo = (TextView) findViewById(R.id.textViewJo);
        final TextView textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        final TextView textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        call1.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                String json = new Gson().toJson(response.body());
                System.out.println(json);
                String jo = response.body().getNom();
                jo += " " + response.body().getCognoms();
                String username = response.body().getUsername();
                String email = response.body().getEmail();
                textViewJo.setText(jo);
                textViewUsername.setText(username);
                textViewEmail.setText(email);



            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
        Call<ResponseBody> call2 = racoAPIService.getMyPhoto();
        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                Bitmap bm = BitmapFactory.decodeStream(response.body().byteStream());
                imageView.setImageBitmap(bm);
                System.out.println("imatge");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("fail");
            }
        });
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            System.out.println(imageURL);
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}

