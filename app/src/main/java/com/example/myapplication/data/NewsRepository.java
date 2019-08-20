package com.example.myapplication.data;


import com.example.myapplication.Constants;
import com.example.myapplication.data.callbacks.QueryCallBack;
import com.example.myapplication.data.callbacks.DataCallBack;
import com.example.myapplication.data.model.DataModel;

import java.util.List;

public interface NewsRepository {
    void getAllNews(DataCallBack<List<DataModel>> callback, Request request);

    void getRecommendedNews(DataCallBack<List<DataModel>> callBack, Request request);

    void changeQuery(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType, String query);

    void changeSource(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType, String source);

    void changeLanguage(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType, String language);

    void changeSortBy(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType, String sortBy);

    void changeCategory(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType, String category);

    void submitRequest(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType, Request request);

    void submitDefaultRequest(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType);

    Request getCurrentRequest();

    void refresh(DataCallBack<List<DataModel>> callBack, Constants.NewsType newsType);

    void saveArticle(String url);

    void checkArticle(QueryCallBack queryCallBack, String url);

}
