package com.example.myapplication.data;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Application;
import com.example.myapplication.data.callback.DataCallBack;
import com.example.myapplication.data.model.DataModel;
import com.example.myapplication.data.model.DataModelCall;
import com.example.myapplication.data.net.Retro;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepositoryImpl implements NewsRepository {


    @Override
    public void getNews(final DataCallBack<List<DataModel>> callBack) {
        final String API_KEY = "47075dc90ef54c6f8a0880b20a3ceffc";
        final List<DataModel> dataModels = Application.getNews();
        if (dataModels == null || dataModels.size() == 0) {
            Call<DataModelCall> repos = Retro.getServiceAll().listRepos("bitcoin", "2019-06-19",
                    "publishedAt", "cnn", API_KEY);

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
        } else {
            callBack.onEmit(dataModels);
        }
    }
}
