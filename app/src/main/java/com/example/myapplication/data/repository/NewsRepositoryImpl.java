package com.example.myapplication.data.repository;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.Constants;
import com.example.myapplication.data.Request;
import com.example.myapplication.data.callbacks.QueryCallBack;
import com.example.myapplication.data.callbacks.DataCallBack;
import com.example.myapplication.data.model.DataModel;
import com.example.myapplication.data.net.RequestGenerator;
import com.example.myapplication.ui.DetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.myapplication.Constants.DEFAULT_REQUEST;

public class NewsRepositoryImpl implements NewsRepository {

    private List<DataModel> listAllNews;

    private List<DataModel> listRecommendedNews;

    private Request currentRequest = DEFAULT_REQUEST;

    private Request previousRequest;

    @Override
    public void setAllNewsList(List<DataModel> news) {
        Log.i("Application", "setNews listRepos " + news.size());
        listAllNews = news;
    }

    private List<DataModel> getAllNewsList() {
        if (listAllNews != null)
            Log.i("Application", "getAllNewsList listRepos " + listAllNews.size());
        else
            Log.i("Application", "getAllNewsList listRepos = null ");
        return listAllNews;
    }

    public void setRecommendedNewsList(List<DataModel> news) {
        listRecommendedNews = news;
    }

    private List<DataModel> getRecommendedNews() {
        return listRecommendedNews;
    }

    @Override
    public void getAllNews(final DataCallBack<List<DataModel>> callBack, Request request) {
        List<DataModel> dataModels = getAllNewsList();
        if (dataModels == null || dataModels.size() == 0 || request.notEquals(previousRequest)) {
            Date date = new Date();
            String modifiedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date);
            RequestGenerator requestGenerator = new RequestGenerator.Builder()
                    .setQuery(request.getQuery())
                    .setFromDate(modifiedDate)
                    .setSortBy(request.getSortBy())
                    .setSource(request.getSource())
                    .setLanguage(request.getLanguage())
                    .build();
            requestGenerator.execute(callBack, Constants.NewsType.ALL);
        } else {
            callBack.onEmit(dataModels);
        }
        callBack.onCompleted();
    }

    @Override
    public void getRecommendedNews(DataCallBack<List<DataModel>> callBack, Request request) {
        Log.d("NewsRepository", "Got here");
        List<DataModel> dataModels = getRecommendedNews();
        if (dataModels == null || dataModels.size() == 0 || request.notEquals(previousRequest)) {
            Date date = new Date();
            String modifiedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date);
            Log.d("NewsRepository", request.getSource());
            Log.d("NewsRepository", "getting new");
            RequestGenerator requestGenerator = new RequestGenerator.Builder()
                    .setQuery(request.getQuery())
                    .setFromDate(modifiedDate)
                    .setSortBy(request.getSortBy())
                    .setSource(request.getSource())
                    .setLanguage(request.getLanguage())
                    .setCategory(request.getCategory())
                    .build();
            requestGenerator.execute(callBack, Constants.NewsType.RECOMMENDED);
        } else {
            Log.d("NewsRepository", "same old");
            callBack.onEmit(dataModels);
        }
        callBack.onCompleted();
    }

    @Override
    public void changeQuery(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType, String query) {
        Request request = new Request(query, currentRequest.getSource(),
                currentRequest.getLanguage(), currentRequest.getSortBy(),
                currentRequest.getCategory());
        submitRequest(callBack, newsType, request);
    }

    @Override
    public void changeSource(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType, String source) {
        Request request = new Request(currentRequest.getQuery(), source, currentRequest.getLanguage(),
                currentRequest.getSortBy(), "");
        submitRequest(callBack, newsType, request);
    }

    @Override
    public void changeLanguage(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType, String language) {
        Request request = new Request(currentRequest.getQuery(), currentRequest.getSource(), language,
                currentRequest.getSortBy(), currentRequest.getCategory());
        submitRequest(callBack, newsType, request);
    }

    @Override
    public void changeSortBy(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType, String sortBy) {
        Request request = new Request(currentRequest.getQuery(), currentRequest.getSource(),
                currentRequest.getLanguage(), sortBy, currentRequest.getCategory());
        submitRequest(callBack, newsType, request);
    }

    @Override
    public void changeCategory(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType, String category) {
        Request request = new Request(currentRequest.getQuery(), "", "",
                currentRequest.getSortBy(), category);
        submitRequest(callBack, newsType, request);
    }

    @Override
    public void submitRequest(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType, Request request) {
            previousRequest = currentRequest;
        if (newsType == Constants.NewsType.RECOMMENDED) {
            getRecommendedNews(callBack, request);
            currentRequest = request;
        } else if (newsType == Constants.NewsType.ALL) {
            getAllNews(callBack, request);
            currentRequest = request;
        }
    }

    @Override
    public void submitDefaultRequest(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType) {
        previousRequest = currentRequest;
        if (newsType == Constants.NewsType.RECOMMENDED) {
            getRecommendedNews(callBack, DEFAULT_REQUEST);
            currentRequest = DEFAULT_REQUEST;
        } else if (newsType == Constants.NewsType.ALL) {
            getAllNews(callBack, DEFAULT_REQUEST);
            currentRequest = DEFAULT_REQUEST;
        }
    }

    @Override
    public void refresh(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType) {
        if (newsType == Constants.NewsType.RECOMMENDED) {
            getRecommendedNews(callBack, currentRequest);
        } else if (newsType == Constants.NewsType.ALL) {
            getAllNews(callBack, currentRequest);
        }
    }

    @Override
    public Request getCurrentRequest() {
        return currentRequest;
    }

    @Override
    public void saveArticle(String url) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference myRef = null;
        if (uid != null) {
            myRef = database.getReference(uid);
        }
        String key = null;
        if (myRef != null) {
            key = myRef.push().getKey();
        }
        if (key != null) {
            myRef.child(key).setValue(url);
        }
    }

    @Override
    public void checkArticle(final QueryCallBack queryCallBack, String url) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getUid();
        Query query = null;
        if (uid != null) {
            query = myRef.child(uid).orderByValue().equalTo(url);
        }
        if (query != null) {
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        queryCallBack.onFound();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public Intent getDetailIntent(Context context, String url) {
        Intent detailIntent = new Intent(context, DetailActivity.class);
        for(DataModel dataModel : listRecommendedNews){
            if(dataModel.getUrl().equals(url)){
                Bundle bundle = new Bundle();
                bundle.putString("title", dataModel.getTitle());
                bundle.putString("description", dataModel.getDescription());
                bundle.putString("content", dataModel.getContent());
                bundle.putString("urlToImage", dataModel.getUrlToImage());
                bundle.putString("source", dataModel.getSource().getName());
                bundle.putString("url", dataModel.getUrl());
                detailIntent.putExtra("info", bundle);
            }
        }
        return detailIntent;
    }
}