package com.example.lucent.model;

import com.google.gson.annotations.SerializedName;

public class Payment {
    @SerializedName("donor")
    Donor donor;
    @SerializedName("organization")
    Organization organization;
    @SerializedName("amount")
    int amount;
    @SerializedName("created")
    String created;

    public Payment(Donor donor, Organization organization, int amount, String created) {
        this.donor = donor;
        this.organization = organization;
        this.amount = amount;
        this.created = created;
    }

    public Donor getDonor() {
        return donor;
    }

    public void setDonor(Donor donor) {
        this.donor = donor;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
