package com.example.lucent.model;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.Url;

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
    public Single<List<Spending>>getSpendings(@Url String url){
        return orgAPI.getSpendings(url);
    }

//    Login
    public Single<LoginResponse> login(@Query("phone") String phone, @Query("password") String password){ return orgAPI.login(phone, password); }

//    Get Profile
    public Single<User> getProfile(@Header("AUTHORIZATION") String bearerToken){ return orgAPI.getProfile(bearerToken);};

//    Registration
    public Single<User> register(@Body RegisterRequest registerRequest) { return orgAPI.register(registerRequest); }
}
