package com.example.myapplication.data.net;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retro {
    final static String baseUrl = "https://newsapi.org/";

    public static GithubService getServiceAll() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(GithubService.class);
    }
}