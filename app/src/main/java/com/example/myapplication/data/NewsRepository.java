package com.example.myapplication.data;

import android.app.Activity;

import androidx.recyclerview.widget.AsyncListUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.callback.DataCallBack;
import com.example.myapplication.data.model.DataModel;

import java.util.List;

public interface NewsRepository {
    void getNews(DataCallBack<List<DataModel>> callback);
}
