package com.example.myapplication;

import android.util.Log;

import com.example.myapplication.data.model.DataModel;

import java.util.List;

public class Application extends android.app.Application {
    private static List<DataModel> listNews;

    public static void setNews(List<DataModel> news) {
        Log.i("Application", "setNews listRepos " + news.size());
        listNews = news;
    }

    public static List<DataModel> getNews() {
        if (listNews != null)
        Log.i("Application", "getNews listRepos " + listNews.size());
        else
            Log.i("Application", "getNews listRepos = null ");
        return listNews;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
