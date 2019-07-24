package com.example.myapplication.ui.newListFragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.Application;
import com.example.myapplication.Constants;
import com.example.myapplication.R;
import com.example.myapplication.Request;
import com.example.myapplication.data.callback.DataCallBack;
import com.example.myapplication.data.model.DataModel;

import java.util.List;


public class NewsListFragment extends Fragment {

    private Request currentRequest;

    private Constants.NewsType newsType;

    RecyclerView recyclerView;

    public NewsListFragment() {
    }

    public static NewsListFragment newInstance(Constants.NewsType newsType) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putSerializable("newsType", newsType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            newsType = (Constants.NewsType) getArguments().getSerializable("newsType");
        }
        Log.i("NewsListFragment", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsitem_list, container, false);

        final SearchView searchView = view.findViewById(R.id.search_bar);

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pull_to_refresh);

        recyclerView = view.findViewById(R.id.list);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Request request = new Request(searchView.getQuery().toString(), currentRequest.getPerspective());
                updateNews(request, recyclerView, newsType);
                currentRequest = request;
                pullToRefresh.setRefreshing(false);
            }
        });





        Log.d("NewsListFragment", "onCreateView");
        // Set the adapter
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

                Request request = new Request("", "cnn");
                updateNews(request, recyclerView, newsType);
                currentRequest = request;


                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Request request = new Request(query, currentRequest.getPerspective());
                        updateNews(request, recyclerView, newsType);
                        currentRequest = request;
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });

        return view;
    }

    private void updateNews(Request request, final RecyclerView recyclerView, Constants.NewsType newsType) {
        if(newsType == Constants.NewsType.ALL) {

            Application.getRepository().getAllNews(new DataCallBack<List<DataModel>>() {

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
            }, newsType, request);
        }
        else if(newsType == Constants.NewsType.RECCOMENDED){
            Application.getRepository().getRecommendedNews(new DataCallBack<List<DataModel>>() {
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
            }, newsType, request);
        }

    }

    public void changePerspective(String perspective){
        Request request = new Request(currentRequest.getQuery(), perspective);
        Log.d("NewsListFragment", currentRequest.getPerspective());
        updateNews(request, recyclerView, newsType);
        currentRequest = request;
    }

}