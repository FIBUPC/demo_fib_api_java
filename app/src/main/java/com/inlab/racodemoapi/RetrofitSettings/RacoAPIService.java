package com.inlab.racodemoapi.RetrofitSettings;

import com.inlab.racodemoapi.Models.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by florencia.rimolo on 14/02/2017.
 */

public interface RacoAPIService {
        @Headers("Accept: application/json")
        @GET("jo/")
        Call<User> getMyInfo(
        );
}
