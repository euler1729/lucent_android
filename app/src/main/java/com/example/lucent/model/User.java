package com.example.lucent.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("name")
    String name;
    @SerializedName("phone")
    String phone;
    @SerializedName("verified")
    Boolean verified;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", verified=" + verified +
                '}';
    }

    public Boolean getVerified() {
        return verified;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
