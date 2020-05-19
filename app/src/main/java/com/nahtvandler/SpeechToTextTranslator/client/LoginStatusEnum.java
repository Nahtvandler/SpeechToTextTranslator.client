package com.nahtvandler.SpeechToTextTranslator.client;

public enum LoginStatusEnum {
    SUCCES("1"), CLIENT_NOT_FOUND("2"), INCORRECT_PASSWORD("3");

    private String status;

    LoginStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
