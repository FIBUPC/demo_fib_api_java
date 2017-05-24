package com.inlab.racodemoapi.ServiceSettings;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by florencia.rimolo on 16/02/2017.
 */

// This is the interface definition which is passed
// to ServiceGenerator to create a Retrofit HTTP client.
public interface LoginService {
    @FormUrlEncoded
    @POST("o/token")
    Call<AccessToken> getAccessToken(
            @Field("grant_type") String grantType,
            @Field("code") String code,
            @Field("redirect_uri") String redirectURI,
            @Field("client_id") String clientID,
            @Field("client_secret") String client_secret);

}
