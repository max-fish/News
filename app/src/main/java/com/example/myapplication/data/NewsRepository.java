package com.example.myapplication.data;


import com.example.myapplication.Constants;
import com.example.myapplication.Request;
import com.example.myapplication.data.callback.DataCallBack;
import com.example.myapplication.data.model.DataModel;

import java.util.List;

public interface NewsRepository {
    void getAllNews(DataCallBack<List<DataModel>> callback, Constants.NewsType newsType, Request request);

    void getRecommendedNews(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType, Request request);

    void saveArticle(String url);

    boolean checkArticle(String url);

}
