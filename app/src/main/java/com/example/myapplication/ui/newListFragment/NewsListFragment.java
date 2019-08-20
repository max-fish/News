package com.example.myapplication.ui.newListFragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;
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
import com.example.myapplication.ui.PreferencesView;

import java.util.List;
import java.util.Objects;


public class NewsListFragment extends Fragment implements DataCallBack<List<DataModel>>,
        View.OnClickListener {

    private Constants.NewsType newsType;
    private RecyclerView recyclerView;
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

        preferencesView = Objects.requireNonNull(getActivity()).findViewById(R.id.filter_selection);

        recyclerView = view.findViewById(R.id.list);

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

        Animation anim = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        anim.setDuration(250);

        recyclerView.setLayoutAnimation(new LayoutAnimationController(anim));


        Application.getRepository().submitDefaultRequest(this, newsType);

        addFilterPreferences();

        return view;
    }

    private void addFilterPreferences() {
        Request currentRequest = Application.getRepository().getCurrentRequest();
        preferencesView.addFilterPreference(this, newsType, currentRequest.getQuery(), Constants.FILTER_PREFERENCE_QUERY_ID);
        preferencesView.addFilterPreference(this, newsType, currentRequest.getSource(), Constants.FILTER_PREFERENCE_SOURCE_ID);
        preferencesView.addFilterPreference(this, newsType, currentRequest.getLanguage(), Constants.FILTER_PREFERENCE_LANGUAGE_ID);
        preferencesView.addFilterPreference(this, newsType, currentRequest.getSortBy(), Constants.FILTER_PREFERENCE_SORT_BY_ID);
        preferencesView.addFilterPreference(this, newsType, currentRequest.getCategory(), Constants.FILTER_PREFERENCE_CATEGORY_ID);
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