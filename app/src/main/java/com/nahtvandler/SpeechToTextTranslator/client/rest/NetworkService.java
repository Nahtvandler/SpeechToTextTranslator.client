package com.nahtvandler.SpeechToTextTranslator.client.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
//    private static final String BASE_URL = "http://api.myjson.com";
    private static final String BASE_URL = "http://192.168.1.67:8081/";
    private Retrofit mRetrofit;

    private NetworkService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public LoginApi getLoginApi() {
        return mRetrofit.create(LoginApi.class);
    }

}