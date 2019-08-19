package com.example.myapplication.data;


import com.example.myapplication.Constants;
import com.example.myapplication.data.callbacks.QueryCallBack;
import com.example.myapplication.data.callbacks.DataCallBack;
import com.example.myapplication.data.model.DataModel;

import java.util.List;

public interface NewsRepository {
    void getAllNews(DataCallBack<List<DataModel>> callback, Constants.NewsType newsType, Request request);

    void getRecommendedNews(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType, Request request);

    void changeQuery(String query);

    void changeSource(String source);

    void changeLanguage(String language);

    void changeSortBy(String sortBy);

    void changeCateogry(String category);

    void submitRequest(String query, String source, String language, String sortBy, String category);

    void submitRequest(Request request);

    void saveArticle(String url);

    void checkArticle(QueryCallBack queryCallBack, String url);

}
