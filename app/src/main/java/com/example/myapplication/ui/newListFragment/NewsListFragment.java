package com.example.myapplication.ui.newListFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.Application;
import com.example.myapplication.Constants;
import com.example.myapplication.R;
import com.example.myapplication.data.Request;
import com.example.myapplication.data.callbacks.DataCallBack;
import com.example.myapplication.data.model.DataModel;
import com.example.myapplication.ui.preferences.PreferencesView;

import java.util.List;
import java.util.Objects;

import static com.example.myapplication.Constants.FilterPreferenceIDs.*;


public class NewsListFragment extends Fragment implements DataCallBack<List<DataModel>>,
        View.OnClickListener {

    private Constants.NewsType newsType;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private PreferencesView preferencesView;

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

        preferencesView = view.findViewById(R.id.filter_selection);

        progressBar = view.findViewById(R.id.news_progress_bar);

        recyclerView = view.findViewById(R.id.list);


        final Animation anim = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        anim.setDuration(250);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Application.getRepository().refresh(NewsListFragment.this, newsType);
                pullToRefresh.setRefreshing(false);
            }
        });

        Log.d("NewsListFragment", "onCreateView");
        // Set the adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setLayoutAnimation(new LayoutAnimationController(anim));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //progressBar.setVisibility(View.VISIBLE);
        if(newsType == Constants.NewsType.RECOMMENDED) {
            Application.getRepository().submitDefaultRequest(this, Constants.NewsType.RECOMMENDED);
        }
        else{
            Application.getRepository().submitDefaultRequest(this, Constants.NewsType.ALL);
        }
        addFilterPreferences();
    }

    private void addFilterPreferences() {
        Request currentRequest = Application.getRepository().getCurrentRequest();
        preferencesView.addFilterPreference(this, newsType, currentRequest.getQuery(), FILTER_PREFERENCE_QUERY_ID);
        preferencesView.addFilterPreference(this, newsType, currentRequest.getSource(), FILTER_PREFERENCE_SOURCE_ID);
        preferencesView.addFilterPreference(this, newsType, currentRequest.getLanguage(), FILTER_PREFERENCE_LANGUAGE_ID);
        preferencesView.addFilterPreference(this, newsType, currentRequest.getSortBy(), FILTER_PREFERENCE_SORT_BY_ID);
        preferencesView.addFilterPreference(this, newsType, currentRequest.getCategory(), FILTER_PREFERENCE_CATEGORY_ID);
    }


    public RecyclerView getRecyclerView(){
        return recyclerView;
    }

    public Constants.NewsType getNewsType(){
        return newsType;
    }

    @Override
    public void onEmit(List<DataModel> data) {
        recyclerView.setAdapter(new MyNewsItemRecyclerViewAdapter(data, Objects.requireNonNull(getActivity()), recyclerView));
    }

    @Override
    public void onCompleted() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(Throwable throwable) {
        if (getActivity() != null)
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

    }
}