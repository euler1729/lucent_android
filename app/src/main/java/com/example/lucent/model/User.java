package com.example.lucent.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("phone")
    String phone;
    @SerializedName("verified")
    Boolean verified;
    @SerializedName("amount")
    int amount;
    @SerializedName("donor")
    Donor donor;
    @SerializedName("created")
    String created;

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", verified=" + verified +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Donor getDonor() {
        return donor;
    }

    public void setDonor(Donor donor) {
        this.donor = donor;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
