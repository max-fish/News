package com.example.myapplication;

import android.util.Log;

import com.example.myapplication.data.NewsRepository;
import com.example.myapplication.data.NewsRepositoryImpl;
import com.example.myapplication.data.model.DataModel;

import java.util.List;

public class Application extends android.app.Application {

    private static List<DataModel> listNews;

    private static List<DataModel> listRecommendedNews;

    public static NewsRepository newsRepository;

    public static NewsRepository getRepository(){
        return newsRepository;
    }

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

    public static void setRecommendedNews(List<DataModel> news){
        listRecommendedNews = news;
    }

    public static List<DataModel> getRecommendedNews(){
        return listRecommendedNews;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        newsRepository = new NewsRepositoryImpl();

    }
}
