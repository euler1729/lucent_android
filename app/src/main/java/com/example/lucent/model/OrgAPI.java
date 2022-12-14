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
    @GET
    Single<List<User>> getLatestDonations(@Url String url);
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
    //refreshes access token
    @GET("token/refresh")
    Single<LoginResponse>getAccessToken(@Header("AUTHORIZATION") String refreshToken);

    @GET
    Single<Membership> checkMembership(@Header("AUTHORIZATION")String bearerToken,@Url String url);

    @HTTP(method = "POST",path="donate",hasBody = true)
    Single<Payment>donate(@Header("AUTHORIZATION")String bearerToken,  @Body PayRequest request);

    @HTTP(method = "POST", path = "membership/request", hasBody = true)
    Single<Membership>requestMembership(@Header("AUTHORIZATION")String bearerToken,  @Body MembershipRequest membershipRequest);

    @HTTP(method = "POST", path = "user/verify", hasBody = true)
    Single<OTPResponse>requestVerification(@Header("AUTHORIZATION") String bearerToken, @Query(value = "phone", encoded = true) String phone, @Query(value = "code", encoded = true) String code);

    @HTTP(method = "GET", path = "user/resendcode")
    Single<OTPresendResponse>resendOTP(@Header("AUTHORIZATION") String bearerToken);

}
