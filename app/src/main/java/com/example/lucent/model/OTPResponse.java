package com.example.lucent.model;

import com.google.gson.annotations.SerializedName;

public class OTPResponse {
    @SerializedName("Success")
    Boolean success;

    public OTPResponse(Boolean success){
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }
}
