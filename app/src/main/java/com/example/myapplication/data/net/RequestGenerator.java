package com.example.myapplication.data.net;

import android.util.Log;

import com.example.myapplication.Application;
import com.example.myapplication.Constants;
import com.example.myapplication.data.callback.DataCallBack;
import com.example.myapplication.data.model.DataModel;
import com.example.myapplication.data.model.DataModelCall;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestGenerator {

    private String query;
    private String fromDate;
    private String sortBy;
    private String source;
    private String language;
    private String category;
    private static final String API_KEY = "47075dc90ef54c6f8a0880b20a3ceffc";


    public RequestGenerator(String query, String fromDate, String sortBy, String source, String language, String category) {
        this.query = query;
        this.fromDate = fromDate;
        this.sortBy = sortBy;
        this.source = source;
        this.language = language;
        this.category = category;
    }

    public void execute(final DataCallBack<List<DataModel>> callBack, final Constants.NewsType newsType) {
        Log.i("NewsListFragment", "Retro.getService().listRepos");

        if (newsType == Constants.NewsType.RECCOMENDED) {

            Call<DataModelCall> reposRecommended = Retro.getServiceRecommended().listRepos(query, fromDate,
                    sortBy, source, language, category, API_KEY);

            reposRecommended.enqueue(new Callback<DataModelCall>() {
                @Override
                public void onResponse(Call<DataModelCall> call, Response<DataModelCall> response) {
                    if (response.body() != null) {
                        List<DataModel> results = response.body().getArticles();
                        callBack.onEmit(results);
                            Application.setRecommendedNews(results);
                    }
                }

                @Override
                public void onFailure(Call<DataModelCall> call, Throwable t) {
                    callBack.onError(t);
                }
            });
        }
        if(newsType == Constants.NewsType.ALL){
            Call<DataModelCall> reposAll = Retro.getServiceAll().listRepos(query, fromDate, sortBy,
                    source, language, API_KEY);
            reposAll.enqueue(new Callback<DataModelCall>() {
                @Override
                public void onResponse(Call<DataModelCall> call, Response<DataModelCall> response) {
                    if (response.body() != null) {
                        List<DataModel> results = response.body().getArticles();
                        callBack.onEmit(results);
                            Application.setNews(results);
                    }
                }

                @Override
                public void onFailure(Call<DataModelCall> call, Throwable t) {
                    callBack.onError(t);
                }
            });
        }
    }

    public static class Builder{
        private String query;
        private String fromDate;
        private String sortBy;
        private String source;
        private String language;
        private String category;

        public Builder(){}


        public Builder setQuery(String query) {
            this.query = query;
            return this;
        }


        public Builder setFromDate(String fromDate) {
            this.fromDate = fromDate;
            return this;
        }

        public Builder setSortBy(String sortBy) {
            this.sortBy = sortBy;
            return this;
        }
        public Builder setSource(String source) {
            this.source = source;
            return this;
        }

        public Builder setLanguage(String language){
            this.language = language;
            return this;
        }

        public Builder setCategory(String category){
            this.category = category;
            return this;
        }

        public RequestGenerator build(){
            return new RequestGenerator(query, fromDate, sortBy, source, language, category);
        }
    }
}
