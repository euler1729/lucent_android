package com.example.lucent.model;

import com.google.gson.annotations.SerializedName;

public class Manager {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;

    public Manager(int id, String name) {
        this.id = id;
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

}
