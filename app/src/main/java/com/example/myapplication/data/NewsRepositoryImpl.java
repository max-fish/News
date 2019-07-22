package com.example.myapplication.data;

import com.example.myapplication.Application;
import com.example.myapplication.data.callback.DataCallBack;
import com.example.myapplication.data.model.DataModel;
import com.example.myapplication.data.net.RequestGenerator;

import java.util.List;

public class NewsRepositoryImpl implements NewsRepository {


    @Override
    public void getNews(final DataCallBack<List<DataModel>> callBack) {
         List<DataModel> dataModels = Application.getNews();
        if (dataModels == null || dataModels.size() == 0) {
            RequestGenerator requestGenerator = new RequestGenerator.Builder()
                    .setQuery("bitcoin")
                    .setFromDate("2019-06-22")
                    .setSortBy("publishedAt")
                    .setSource("cnn")
                    .build();
            requestGenerator.execute(callBack);
        } else {
            callBack.onEmit(dataModels);
        }
    }
}
