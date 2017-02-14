package com.inlab.racodemoapi.RetrofitSettings;

import com.inlab.racodemoapi.Models.PlansEstudiModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by florencia.rimolo on 14/02/2017.
 */

public interface RacoAPIService {
        @GET("/plans_estudi")
        Call<List<PlansEstudiModel>> plansEstudi(
        );
}
