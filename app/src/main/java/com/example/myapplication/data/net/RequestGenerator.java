package com.example.myapplication.data.net;

import android.app.Activity;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Application;
import com.example.myapplication.data.callback.DataCallBack;
import com.example.myapplication.data.model.DataModel;
import com.example.myapplication.data.model.DataModelCall;
import com.example.myapplication.ui.newListFragment.MyNewsItemRecyclerViewAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestGenerator {

    private String query;
    private String fromDate;
    private String sortBy;
    private String source;
    private static final String API_KEY = "47075dc90ef54c6f8a0880b20a3ceffc";
    private List<DataModel> newsList;


    public RequestGenerator(String query, String fromDate, String sortBy, String source) {
        this.query = query;
        this.fromDate = fromDate;
        this.sortBy = sortBy;
        this.source = source;
    }

    public void execute(final DataCallBack<List<DataModel>> callBack) {
        Log.i("NewsListFragment", "Retro.getService().listRepos");

            Call<DataModelCall> repos = Retro.getServiceAll().listRepos(query, fromDate,
                    sortBy, source, API_KEY);

        repos.enqueue(new Callback<DataModelCall>() {
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

    public static class Builder{
        private String query;
        private String fromDate;
        private String sortBy;
        private String source;

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

        public RequestGenerator build(){
            return new RequestGenerator(query, fromDate, sortBy, source);
        }
    }
}
