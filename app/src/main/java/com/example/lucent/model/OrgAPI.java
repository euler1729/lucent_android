package com.example.lucent.model;


import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface OrgAPI {
    @GET("org/published?page=0&size=10&sortBy=id")
    Single<List<Organization>> getOrgs();
    @GET
    Single<List<Spending>> getSpendings(@Url String url);
}
