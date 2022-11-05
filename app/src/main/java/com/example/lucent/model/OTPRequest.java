package com.example.lucent.model;

import com.google.gson.annotations.SerializedName;

public class OTPRequest {
    @SerializedName("phone")
    String phone;
    @SerializedName("code")
    String code;

    public OTPRequest(String _phone, String _code){
        this.phone = _phone;
        this.code = _code;
    }

    public String getPhone() {
        return phone;
    }

    public String getCode() {
        return code;
    }
}
