package com.example.lucent.model;

import com.google.gson.annotations.SerializedName;

public class Organization {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("description")
    String description;
    boolean autoApprove;
    boolean requireCode;
    boolean requireNID;
    int balance;
    int spent;
    @SerializedName("manager")
    Manager manager;
    int manager_id;
    String manager_name;
    boolean published;
    String profilePicURL;
    String coverPicURL;
    int memberCount;


    public Organization(){

    }
    public Organization(int id, String name, String description, boolean autoApprove, boolean requireCode, boolean requireNID, int balance, int spent, int manager_id, String manager_name, boolean published, String profilePicURL, String coverPicURL, int memberCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.autoApprove = autoApprove;
        this.requireCode = requireCode;
        this.requireNID = requireNID;
        this.balance = balance;
        this.spent = spent;
        this.manager_id = manager_id;
        manager = new Manager(manager_id,manager_name);
        this.manager_name = manager_name;
        this.published = published;
        this.profilePicURL = profilePicURL;
        this.coverPicURL = coverPicURL;
        this.memberCount = memberCount;
    }
    public Organization(String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAutoApprove() {
        return autoApprove;
    }

    public void setAutoApprove(boolean autoApprove) {
        this.autoApprove = autoApprove;
    }

    public boolean isRequireCode() {
        return requireCode;
    }

    public void setRequireCode(boolean requireCode) {
        this.requireCode = requireCode;
    }

    public boolean isRequireNID() {
        return requireNID;
    }

    public void setRequireNID(boolean requireNID) {
        this.requireNID = requireNID;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getSpent() {
        return spent;
    }

    public void setSpent(int spent) {
        this.spent = spent;
    }

    public int getManager_id() {
        return manager_id;
    }

    public void setManager_id(int manager_id) {
        this.manager_id = manager_id;
    }

    public String getManager_name() {
        return manager_name;
    }

    public void setManager_name(String manager_name) {
        this.manager_name = manager_name;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public String getCoverPicURL() {
        return coverPicURL;
    }

    public void setCoverPicURL(String coverPicURL) {
        this.coverPicURL = coverPicURL;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }
}
