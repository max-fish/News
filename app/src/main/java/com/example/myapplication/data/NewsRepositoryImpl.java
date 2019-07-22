package com.example.myapplication.data;

import android.util.Log;

import com.example.myapplication.Application;
import com.example.myapplication.Constants;
import com.example.myapplication.data.callback.DataCallBack;
import com.example.myapplication.data.model.DataModel;
import com.example.myapplication.data.net.RequestGenerator;

import java.util.List;

public class NewsRepositoryImpl implements NewsRepository {


    @Override
    public void getAllNews(final DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType, String query, boolean refresh) {
        if (!refresh) {
            List<DataModel> dataModels = Application.getNews();
            if (dataModels == null || dataModels.size() == 0) {
                RequestGenerator requestGenerator = new RequestGenerator.Builder()
                        .setQuery(query)
                        .setFromDate("2019-06-22")
                        .setSortBy("publishedAt")
                        .setSource("cnn")
                        .setLanguage("en")
                        .build();
                requestGenerator.execute(callBack, newsType);
            } else {
                callBack.onEmit(dataModels);
            }
        } else {
            RequestGenerator requestGenerator = new RequestGenerator.Builder()
                    .setQuery(query)
                    .setFromDate("2019-06-22")
                    .setSortBy("publishedAt")
                    .setSource("cnn")
                    .setLanguage("en")
                    .build();
            requestGenerator.execute(callBack, newsType);
        }
    }

    @Override
    public void getRecommendedNews(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType, String query, boolean refresh) {
        if (!refresh) {
            List<DataModel> dataModels = Application.getRecommendedNews();

            if (dataModels == null || dataModels.size() == 0 || !query.equals(Application.getQuery())) {
                Log.d("NewsRepository", "getting new");
                Application.addQuery(query);
                RequestGenerator requestGenerator = new RequestGenerator.Builder()
                        .setQuery(query)
                        .setFromDate("2019-06-22")
                        .setSortBy("publishedAt")
                        .setSource("bbc-news")
                        .setLanguage("en")
                        .build();
                requestGenerator.execute(callBack, newsType);
            } else {
                callBack.onEmit(dataModels);
            }
        } else {
            RequestGenerator requestGenerator = new RequestGenerator.Builder()
                    .setQuery(query)
                    .setFromDate("2019-06-22")
                    .setSortBy("publishedAt")
                    .setLanguage("en")
                    .build();
            requestGenerator.execute(callBack, newsType);
        }
    }
}