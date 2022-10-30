package com.example.lucent.model;

import com.google.gson.annotations.SerializedName;

public class Membership {
    @SerializedName("id")
    int id;
    @SerializedName("donor")
    String donor;
    @SerializedName("organization")
    String organization;
    @SerializedName("nid")
    String nid;
    @SerializedName("membershipCode")
    String membershipCode;
    @SerializedName("approved")
    boolean approved;

    public Membership(int id, String donor, String organization, String nid, String membershipCode, boolean approved) {
        this.id = id;
        this.donor = donor;
        this.organization = organization;
        this.nid = nid;
        this.membershipCode = membershipCode;
        this.approved = approved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDonor() {
        return donor;
    }

    public void setDonor(String donor) {
        this.donor = donor;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getMembershipCode() {
        return membershipCode;
    }

    public void setMembershipCode(String membershipCode) {
        this.membershipCode = membershipCode;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
