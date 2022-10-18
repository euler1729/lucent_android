package com.example.lucent.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("refresh_token")
    String refresh_token;
    @SerializedName("access_token")
    String access_token;

    public LoginResponse(){

    }
    public LoginResponse(String refresh_token, String access_token) {
        this.refresh_token = refresh_token;
        this.access_token = access_token;
    }

    @NonNull
    @Override
    public String toString() {
        return "LoginResponse{" +
                "refresh_token='" + refresh_token + '\'' +
                ", access_token='" + access_token + '\'' +
                '}';
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }
}
