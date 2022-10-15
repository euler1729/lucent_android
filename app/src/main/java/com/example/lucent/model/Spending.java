package com.example.lucent.model;

import com.google.gson.annotations.SerializedName;

public class Spending {
    @SerializedName("id")
    int id;
    @SerializedName("description")
    String description;
    @SerializedName("amount")
    int amount;
    @SerializedName("collectedAmount")
    int collectedAmount;
    @SerializedName("created")
    String created;

    public Spending(int id, String description, int amount, int collectedAmount, String created) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.collectedAmount = collectedAmount;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCollectedAmount() {
        return collectedAmount;
    }

    public void setCollectedAmount(int collectedAmount) {
        this.collectedAmount = collectedAmount;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
