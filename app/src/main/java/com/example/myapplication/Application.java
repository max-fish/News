package com.example.myapplication;

import java.util.List;

public class Application extends android.app.Application {
    private static List<DataModel> listNews;

    public static void setNews(List<DataModel> news){
        listNews = news;
    }

    public static List<DataModel> getNews(){
        return listNews;
    }
}
