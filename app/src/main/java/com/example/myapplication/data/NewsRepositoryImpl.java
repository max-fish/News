package com.example.myapplication.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.Application;
import com.example.myapplication.Constants;
import com.example.myapplication.QueryCallBack;
import com.example.myapplication.Request;
import com.example.myapplication.data.callback.DataCallBack;
import com.example.myapplication.data.model.DataModel;
import com.example.myapplication.data.net.RequestGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewsRepositoryImpl implements NewsRepository {

    @Override
    public void getAllNews(final DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType, Request request) {
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
                        .setSource(request.getPerspective())
                        .setLanguage(request.getLanguage())
                        .build();
                requestGenerator.execute(callBack, newsType);
            } else {
                Application.addAllRequest(request);
                callBack.onEmit(dataModels);
            }
    }

    @Override
    public void getRecommendedNews(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType, Request request) {
        Log.d("NewsRepository", "Got here");
            List<DataModel> dataModels = Application.getRecommendedNews();
            if (dataModels == null || dataModels.size() == 0 || Application.getRecommendedRequestStack().isEmpty()
                    || !request.equals(Application.getDeletePreviousRecommendedRequest())) {
                Date date = new Date();
                String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                Application.addRecommendedRequest(request);
                Log.d("NewsRepository", request.getPerspective());
                Log.d("NewsRepository", "getting new");
                RequestGenerator requestGenerator = new RequestGenerator.Builder()
                        .setQuery(request.getQuery())
                        .setFromDate(modifiedDate)
                        .setSortBy(request.getSortBy())
                        .setSource(request.getPerspective())
                        .setLanguage(request.getLanguage())
                        .setCategory(request.getCategory())
                        .build();
                requestGenerator.execute(callBack, newsType);
                callBack.onCompleted();
            } else {
                Log.d("NewsRepository", "same old");
                Application.addRecommendedRequest(request);
                callBack.onEmit(dataModels);
            }
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