package com.example.myapplication.ui.newListFragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Application;
import com.example.myapplication.data.model.DataModel;
import com.example.myapplication.data.model.DataModelCall;
import com.example.myapplication.R;
import com.example.myapplication.data.net.Retro;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsListFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount;

    public NewsListFragment() {
    }

    public static NewsListFragment newInstance(int columnCount) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        Log.i("NewsListFragment", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsitem_list, container, false);
        Log.i("NewsListFragment", "onCreateView");
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            final List<DataModel> dataModels = Application.getNews();

            if (dataModels == null || dataModels.size() == 0) {
                Log.i("NewsListFragment", "Retro.getService().listRepos");
                Call<DataModelCall> repos = Retro.getService().listRepos("bitcoin", "2019-06-19",
                    "publishedAt", "bbc-news", "47075dc90ef54c6f8a0880b20a3ceffc");
                repos.enqueue(new Callback<DataModelCall>() {
                    @Override
                    public void onResponse(Call<DataModelCall> call, Response<DataModelCall> response) {
                        if (response.body() != null) {
                            Application.setNews(response.body().getArticles());
                            recyclerView.setAdapter(new MyNewsItemRecyclerViewAdapter(response.body().getArticles(), getActivity()));
                        }
                    }

                    @Override
                    public void onFailure(Call<DataModelCall> call, Throwable t) {

                    }
                });
            }else {
                recyclerView.setAdapter(new MyNewsItemRecyclerViewAdapter(dataModels, getActivity()));
            }
        }
        return view;
    }



}
