package com.example.myapplication;

import android.util.Log;

import com.example.myapplication.data.NewsRepository;
import com.example.myapplication.data.NewsRepositoryImpl;
import com.example.myapplication.data.model.DataModel;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

public class Application extends android.app.Application {

    private static List<DataModel> listNews;

    private static List<DataModel> listRecommendedNews;

    private static Stack<Request> recommendedRequestStack = new Stack<>();

    private static Stack<Request> allRequestStack = new Stack<>();

    private static HashMap<String, String> countriesToISO = new HashMap<>();

    private static NewsRepository newsRepository;


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

    public static void addRecommendedRequest(Request request){
        recommendedRequestStack.push(request);
    }

    public static void addAllRequest(Request request){
        allRequestStack.push(request);
    }

    public static Stack<Request> getRecommendedRequestStack(){return recommendedRequestStack;}

    public static Stack<Request> getAllRequestStack() {return allRequestStack;}

    public static Request getDeletePreviousRecommendedRequest(){return recommendedRequestStack.pop();}

    public static Request getDeletePreviousAllRequest(){return  allRequestStack.pop();}

    private static String location;

    public static void setLocation(String country){
        location = country;
    }

    public static String getLocation(){
        return location;
    }

    public static HashMap<String, String> getCountriesToISO(){
        return countriesToISO;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        newsRepository = new NewsRepositoryImpl();

        for(String iso : Locale.getISOCountries()){
            Locale locale = new Locale("", iso);
            countriesToISO.put(locale.getDisplayCountry(), iso);
        }

    }
}
