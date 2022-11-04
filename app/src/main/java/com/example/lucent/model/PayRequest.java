package com.example.lucent.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class PayRequest {
    @SerializedName("organizationID")
    int organizationID;
    @SerializedName("amount")
    int amount;
    @SerializedName("gateway")
    String gateway;

    public PayRequest(int organizationID, int amount, String gateway) {
        this.organizationID = organizationID;
        this.amount = amount;
        this.gateway = gateway;
    }

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "organizationID='" + organizationID + '\'' +
                ", amount='" + amount + '\'' +
                ", gateway=" + gateway +
                '}';
    }

    public int getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(int organizationID) {
        this.organizationID = organizationID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }
}
