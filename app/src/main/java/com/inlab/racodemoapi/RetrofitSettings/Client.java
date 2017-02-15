package com.inlab.racodemoapi.RetrofitSettings;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by florencia.rimolo on 14/02/2017.
 */

public class Client {
    private static final String API_BASE_URL = "https://api.fib.upc.edu/v2/";

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    /**
     * Get Retrofit Instance
     */
    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Get API Service
     *
     * @return API Service
     */
    public static RacoAPIService getApiService() {
        return getRetrofitInstance().create(RacoAPIService.class);
    }

}
