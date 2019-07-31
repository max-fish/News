package com.example.myapplication.data;

import android.util.Log;

import com.example.myapplication.Application;
import com.example.myapplication.Constants;
import com.example.myapplication.Request;
import com.example.myapplication.data.callback.DataCallBack;
import com.example.myapplication.data.model.DataModel;
import com.example.myapplication.data.net.RequestGenerator;

import java.util.List;

public class NewsRepositoryImpl implements NewsRepository {


    @Override
    public void getAllNews(final DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType, Request request) {
            List<DataModel> dataModels = Application.getNews();
            if (dataModels == null || dataModels.size() == 0) {
                RequestGenerator requestGenerator = new RequestGenerator.Builder()
                        .setQuery(request.getQuery())
                        .setFromDate("2019-06-30")
                        .setSortBy("publishedAt")
                        .setSource(request.getPerspective())
                        .setLanguage("en")
                        .build();
                requestGenerator.execute(callBack, newsType);
            } else {
                callBack.onEmit(dataModels);
            }
    }

    @Override
    public void getRecommendedNews(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType, Request request) {
        Log.d("NewsRepository", "Got here");
            List<DataModel> dataModels = Application.getRecommendedNews();
            if (dataModels == null || dataModels.size() == 0 || Application.getRequestStack().isEmpty()
                    || !request.equals(Application.getDeletePreviousRequest())) {
                Application.addRequest(request);
                Log.d("NewsRepository", request.getPerspective());
                Log.d("NewsRepository", "getting new");
                RequestGenerator requestGenerator = new RequestGenerator.Builder()
                        .setQuery(request.getQuery())
                        .setFromDate("2019-06-30")
                        .setSortBy(request.getSortBy())
                        .setSource(request.getPerspective())
                        .setLanguage(request.getLanguage())
                        .build();
                requestGenerator.execute(callBack, newsType);
            } else {
                Log.d("NewsRepository", "same old");
                Application.addRequest(request);
                callBack.onEmit(dataModels);
            }
    }
}