package com.nahtvandler.SpeechToTextTranslator.client.rest.DTOs;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginPair {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("client")
    @Expose
    private ClientHttpDto client;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ClientHttpDto getClient() {
        return client;
    }

    public void setClient(ClientHttpDto client) {
        this.client = client;
    }
}
