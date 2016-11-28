package com.opendev.buket.club;

import com.opendev.buket.club.web.response.AlphaPayResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface AlphaApiInterface {


    @GET("register.do")
    Call<AlphaPayResponse> pay(@Query("userName") String username,
                               @Query("password") String password,
                               @Query("orderNumber") String orderNumber,
                               @Query("amount") String amount,
                               @Query("returnUrl") String returnUrl,
                               @Query("failUrl") String failUrl,
                               @Query("pageView") String pageView);


}
