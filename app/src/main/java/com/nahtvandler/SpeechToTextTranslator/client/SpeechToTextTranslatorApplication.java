package com.nahtvandler.SpeechToTextTranslator.client;

import android.app.Application;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class SpeechToTextTranslatorApplication extends Application {
    private static SpeechToTextTranslatorApplication application;
    private StompClient stompClient;
    private Subscriptions subscriptions;


    //    String url = "ws://10.0.2.2:8080/ws/websocket";
    String url = "ws://192.168.1.71:8080/ws/websocket";


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        this.initWebsocket();
    }

    public void initWebsocket() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url);
        stompClient.connect();
        subscriptions = new Subscriptions();
    }

    public static SpeechToTextTranslatorApplication instance() {
        return application;
    }

    public StompClient getStompClient() {
        return stompClient;
    }

    public Subscriptions getSubscriptions() {
        return subscriptions;
    }
}
