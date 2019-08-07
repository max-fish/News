package com.example.myapplication.data.net;

import com.example.myapplication.data.model.DataModelCall;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GithubServiceRecommended {
    @GET("/v2/top-headlines")
    Call<DataModelCall> listRepos(@Query("q") String q,
                                  @Query("from") String from,
                                  @Query("sortBy") String sortBy,
                                  @Query("sources") String source,
                                  @Query("language") String language,
                                  @Query("category") String category,
                                  @Query("country") String country,
                                  @Query("apiKey") String apiKey);
}
