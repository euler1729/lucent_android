package com.example.lucent.model;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class API{
    private static final String BASE_URL = "http://ec2-3-17-67-232.us-east-2.compute.amazonaws.com:8080/";
    private OrgAPI orgAPI;

    public API(){
        orgAPI = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(OrgAPI.class);
    }
    public Single<List<Organization>> getOrgs(){
        return orgAPI.getOrgs();
    }
}
