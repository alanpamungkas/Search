package com.example.githubusersearch.networks.interfaces;

import com.example.githubusersearch.models.SearchUserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GithubAPI {

    @GET("search/users")
    Call<SearchUserResponse> getUserList(@Query("q") String keywords,
                                         @Query("page") String page,
                                         @Query("per_page") String perPage);

}
