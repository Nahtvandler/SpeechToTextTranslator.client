package com.nahtvandler.SpeechToTextTranslator.client.rest;

import com.nahtvandler.SpeechToTextTranslator.client.rest.DTOs.ClientHttpDto;
import com.nahtvandler.SpeechToTextTranslator.client.rest.DTOs.RoomHttpDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ChatRoomApi {
    @POST("/SpeechToTextTranslator.server/createRoom")
    public Call<RoomHttpDto> createRoom(@Body RoomHttpDto request);

    @GET("/SpeechToTextTranslator.server/connectToRoom")
    public Call<RoomHttpDto> connect(@Query("id") String id);
}
