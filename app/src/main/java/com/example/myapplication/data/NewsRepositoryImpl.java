package com.example.myapplication.data;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.Application;
import com.example.myapplication.Constants;
import com.example.myapplication.data.callbacks.QueryCallBack;
import com.example.myapplication.data.callbacks.DataCallBack;
import com.example.myapplication.data.model.DataModel;
import com.example.myapplication.data.net.RequestGenerator;
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

public class NewsRepositoryImpl implements NewsRepository {

    private Request currentRequest = Constants.DEFAULT_REQUEST;

    @Override
    public void getAllNews(final DataCallBack<List<DataModel>> callBack, Request request) {
            List<DataModel> dataModels = Application.getNews();
            if (dataModels == null || dataModels.size() == 0 || Application.getAllRequestStack().isEmpty()
                    || !request.equals(Application.getDeletePreviousAllRequest())) {
                Date date = new Date();
                String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                Application.addAllRequest(request);
                RequestGenerator requestGenerator = new RequestGenerator.Builder()
                        .setQuery(request.getQuery())
                        .setFromDate(modifiedDate)
                        .setSortBy(request.getSortBy())
                        .setSource(request.getSource())
                        .setLanguage(request.getLanguage())
                        .build();
                requestGenerator.execute(callBack, Constants.NewsType.ALL);
            } else {
                Application.addAllRequest(request);
                callBack.onEmit(dataModels);
            }
    }

    @Override
    public void getRecommendedNews(DataCallBack<List<DataModel>> callBack, Request request) {
        Log.d("NewsRepository", "Got here");
            List<DataModel> dataModels = Application.getRecommendedNews();
            if (dataModels == null || dataModels.size() == 0 || Application.getRecommendedRequestStack().isEmpty()
                    || !request.equals(Application.getDeletePreviousRecommendedRequest())) {
                Date date = new Date();
                String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                Application.addRecommendedRequest(request);
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
                callBack.onCompleted();
            } else {
                Log.d("NewsRepository", "same old");
                Application.addRecommendedRequest(request);
                callBack.onEmit(dataModels);
            }
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
        if(newsType == Constants.NewsType.RECOMMENDED){
            getRecommendedNews(callBack, request);
            currentRequest = request;
        }
        else if(newsType == Constants.NewsType.ALL){
            getAllNews(callBack, request);
            currentRequest = request;
        }
    }

    @Override
    public void submitDefaultRequest(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType) {
        if(newsType == Constants.NewsType.RECOMMENDED){
            getRecommendedNews(callBack, Constants.DEFAULT_REQUEST);
            currentRequest = Constants.DEFAULT_REQUEST;
        }
        else if(newsType == Constants.NewsType.ALL){
            getAllNews(callBack, Constants.DEFAULT_REQUEST);
            currentRequest = Constants.DEFAULT_REQUEST;
        }
    }

    public void refresh(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType){
        if(newsType == Constants.NewsType.RECOMMENDED){
            getRecommendedNews(callBack, currentRequest);
        }
        else if(newsType == Constants.NewsType.ALL){
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
        DatabaseReference myRef = database.getReference(FirebaseAuth.getInstance().getUid());
        String key = myRef.push().getKey();
        myRef.child(key).setValue(url);
    }

    @Override
    public void checkArticle(final QueryCallBack queryCallBack, String url) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        Query query = myRef.child(FirebaseAuth.getInstance().getUid()).orderByValue().equalTo(url);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    queryCallBack.onFound();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}