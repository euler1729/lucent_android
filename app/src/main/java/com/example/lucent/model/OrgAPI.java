package com.example.lucent.model;


import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface OrgAPI {
    @GET("org/published?page=0&size=10&sortBy=id")
    Single<List<Organization>> getOrgs();
    @GET
    Single<List<Spending>> getSpendings(@Url String url);

//    Login
    @POST("user/login")
    Single<LoginResponse> login(@Query("phone") String phone, @Query("password") String password);

//    Get Profile Information
    @GET("user/profile")
    Single<User> getProfile(@Header("AUTHORIZATION") String bearerToken);

//    Register User
//    @GET("user/registration")
    @HTTP(method = "POST", path = "user/registration", hasBody = true)
    Single<User> register(@Body RegisterRequest registerRequest);

    //Gets subscribed organizations
    @GET("org/my")
    Single<List<Organization>>getMyOrgs(@Header("AUTHORIZATION") String bearerToken);

}
