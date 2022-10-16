package com.example.lucent.model;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("name")
    String name;
    @SerializedName("phone")
    String phone;
    @SerializedName("password")
    String password;

    public RegisterRequest(String name, String phone, String password){
        this.name = name;
        this.password = password;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", password=" + password +
                '}';
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
