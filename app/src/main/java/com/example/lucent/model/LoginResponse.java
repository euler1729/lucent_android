package com.example.lucent.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("refresh_token")
    String refresh_token;
    @SerializedName("access_token")
    String access_token;

    @NonNull
    @Override
    public String toString() {
        return "LoginResponse{" +
                "refresh_token='" + refresh_token + '\'' +
                ", access_token='" + access_token + '\'' +
                '}';
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }
}
