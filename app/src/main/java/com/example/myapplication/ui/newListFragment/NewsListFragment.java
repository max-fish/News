package com.example.myapplication.ui.newListFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import java.util.Objects;


public class NewsListFragment extends Fragment {

    private Request currentRequest;
    private Constants.NewsType newsType;
    private RecyclerView recyclerView;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pull_to_refresh);

        recyclerView = view.findViewById(R.id.list);


        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                submitRequest(currentRequest.getQuery(), currentRequest.getPerspective(), currentRequest.getLanguage(), currentRequest.getSortBy(), currentRequest.getCategory());
                pullToRefresh.setRefreshing(false);
            }
        });

        Log.d("NewsListFragment", "onCreateView");
        // Set the adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        Animation anim = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        anim.setDuration(250);

        recyclerView.setLayoutAnimation(new LayoutAnimationController(anim));

        submitRequest("", "cnn", "en", "publishedAt", "");

        return view;
    }

    private void updateNews(Request request, final RecyclerView recyclerView, Constants.NewsType newsType) {
        if (newsType == Constants.NewsType.ALL) {
            Application.getRepository().getAllNews(new DataCallBack<List<DataModel>>() {
                @Override
                public void onEmit(List<DataModel> data) {
                    recyclerView.setAdapter(new MyNewsItemRecyclerViewAdapter(data, Objects.requireNonNull(getActivity())));

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
        } else if (newsType == Constants.NewsType.RECCOMENDED) {
            Application.getRepository().getRecommendedNews(new DataCallBack<List<DataModel>>() {
                @Override
                public void onEmit(List<DataModel> data) {
                    recyclerView.setAdapter(new MyNewsItemRecyclerViewAdapter(data, Objects.requireNonNull(getActivity())));
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


    public void changeQuery(String query) {
        submitRequest(query, currentRequest.getPerspective(), currentRequest.getLanguage(), currentRequest.getSortBy(), currentRequest.getCategory());
    }

    public void changePerspective(String perspective) {
        submitRequest(currentRequest.getQuery(), perspective, currentRequest.getLanguage(), currentRequest.getSortBy(), "");
    }

    public void changeLanguage(String language){
        submitRequest(currentRequest.getQuery(), currentRequest.getPerspective(), language, currentRequest.getSortBy(), currentRequest.getCategory());
    }

    public void changeSortBy(String sortBy){
        submitRequest(currentRequest.getQuery(), currentRequest.getPerspective(), currentRequest.getLanguage(), sortBy, currentRequest.getCategory());
    }

    public void changeCategory(String category){
        submitRequest(currentRequest.getQuery(), "", currentRequest.getLanguage(), currentRequest.getSortBy(), category);
    }

    private void submitRequest(String query, String perspective, String language, String sortBy, String category) {
        Request request = new Request(query, perspective, language, sortBy, category);
        updateNews(request, recyclerView, newsType);
        currentRequest = request;
    }

    public Request getCurrentRequest() {
        return currentRequest;
    }

    public RecyclerView getRecyclerView(){
        return recyclerView;
    }

}