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

public class API {
    private static final String BASE_URL = "http://ec2-3-17-67-232.us-east-2.compute.amazonaws.com:8080/";
    private final OrgAPI orgAPI;

    public API() {
        orgAPI = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(OrgAPI.class);
    }

    //Get Top Organizations for Home
    public Single<List<Organization>> getOrgs() {
        return orgAPI.getOrgs();
    }
    //Gets Organization details
    public Single<List<Spending>> getSpendings(@Url String url) {
        return orgAPI.getSpendings(url);
    }
    //Gets Latest Donations
    public Single<List<User>> getLatestDonatios(@Url String url){
        return orgAPI.getLatestDonations(url);
    }
    //Login
    public Single<LoginResponse> login(@Query("phone") String phone, @Query("password") String password) {
        return orgAPI.login(phone, password);
    }
    //Get Profile
    public Single<User> getProfile(@Header("AUTHORIZATION") String bearerToken) {
        return orgAPI.getProfile(bearerToken);
    }
    //Registration
    public Single<User> register(@Body RegisterRequest registerRequest) {
        return orgAPI.register(registerRequest);
    }
    //Gets myOrganizations
    public Single<List<Organization>> getMyOrgs(@Header("AUTHORIZATION") String bearerToken) {
        return orgAPI.getMyOrgs(bearerToken);
    }
    public Single<LoginResponse> getAccessToken(@Header("AUTHORIZATION") String refreshToken) {
        return orgAPI.getAccessToken(refreshToken);
    }
    public Single<Membership> checkMembersip(@Header("AUTHORIZATION") String bearerToken, @Url String url){
        return orgAPI.checkMembership(bearerToken,url);
    }
}
