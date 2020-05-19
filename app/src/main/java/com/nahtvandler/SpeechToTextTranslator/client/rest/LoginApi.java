package com.nahtvandler.SpeechToTextTranslator.client.rest;

import com.nahtvandler.SpeechToTextTranslator.client.rest.DTOs.ClientHttpDto;
import com.nahtvandler.SpeechToTextTranslator.client.rest.DTOs.LoginPair;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginApi {
    @POST("/SpeechToTextTranslator.server/registration")
    public Call<String> registeration(@Body ClientHttpDto Data);

    @GET("/SpeechToTextTranslator.server/login")
    public Call<LoginPair> login(@Query("login") String login, @Query("password") String password);
}

