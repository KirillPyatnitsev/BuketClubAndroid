package com.opendev.buket.club;


import com.opendev.buket.club.model.geocoding.Results;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleApiInterface {
    @GET("json")
    Call<Results> pay(@Query("address") String address,
                      @Query("key") String apiKey);
}
