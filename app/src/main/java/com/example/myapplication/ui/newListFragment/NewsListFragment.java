package com.example.myapplication.ui.newListFragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.AsyncListUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Application;
import com.example.myapplication.R;
import com.example.myapplication.data.callback.DataCallBack;
import com.example.myapplication.data.model.DataModel;
import com.example.myapplication.data.net.RequestGenerator;

import java.util.List;


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

            updateNews(recyclerView);
        }

        return view;
    }

    private void updateNews(final RecyclerView recyclerView) {
        Application.getRepository().getNews(new DataCallBack<List<DataModel>>() {

            @Override
            public void onEmit(List<DataModel> data) {
                recyclerView.setAdapter(new MyNewsItemRecyclerViewAdapter(data, getActivity()));
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                if (getActivity() != null)
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}