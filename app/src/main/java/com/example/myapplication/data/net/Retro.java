package com.example.myapplication.data.net;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retro {
    final static String baseUrl = "https://newsapi.org/";

    public static GithubServiceRecommended getServiceRecommended() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(GithubServiceRecommended.class);
    }
    public static GithubServiceAll getServiceAll(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(GithubServiceAll.class);
    }
}