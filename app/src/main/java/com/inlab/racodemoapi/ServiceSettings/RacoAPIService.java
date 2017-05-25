package com.inlab.racodemoapi.ServiceSettings;

import com.inlab.racodemoapi.Models.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;


public interface RacoAPIService {
        /**
         * Here we have all the calls to the API endpoints.
         */
        @Headers("Accept: application/json")
        @GET("jo/")
        Call<User> getMyInfo(
        );
}
