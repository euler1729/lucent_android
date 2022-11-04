package com.example.lucent.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class MembershipRequest {
    @SerializedName("organizationId")
    int organizationId;
    @SerializedName("nid")
    String nid;
    @SerializedName("membershipCode")
    String membershipCode;

    public MembershipRequest(int organizationId, String nid, String membershipCode) {
        this.organizationId = organizationId;
        this.nid = nid;
        this.membershipCode = membershipCode;
    }
    @NonNull
    @Override
    public String toString() {
        return "{" +
                "organizationId='" + organizationId + '\'' +
                ", nid='" + nid + '\'' +
                ", gateway=" + membershipCode +
                '}';
    }
    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
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
}
