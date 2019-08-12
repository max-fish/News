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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.example.myapplication.Request;
import com.example.myapplication.data.callback.DataCallBack;
import com.example.myapplication.data.model.DataModel;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;


public class NewsListFragment extends Fragment {

    private Request currentRequest;
    private Constants.NewsType newsType;
    private RecyclerView recyclerView;
    private LinearLayout filterSelection;

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

        filterSelection = Objects.requireNonNull(getActivity()).findViewById(R.id.filter_selection);

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
                submitRequest(currentRequest.getQuery(), currentRequest.getPerspective(),
                        currentRequest.getLanguage(), currentRequest.getSortBy(),
                        currentRequest.getCategory());
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

        addFilterPreferences();

        return view;
    }

    private void updateNews(Request request, final RecyclerView recyclerView, Constants.NewsType newsType) {
        if (newsType == Constants.NewsType.ALL) {
            Application.getRepository().getAllNews(new DataCallBack<List<DataModel>>() {
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
            }, newsType, request);
        } else if (newsType == Constants.NewsType.RECCOMENDED) {
            Application.getRepository().getRecommendedNews(new DataCallBack<List<DataModel>>() {
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
            }, newsType, request);
        }

    }


    public void changeQuery(String query) {
        removeFilterPreferences(Constants.FILTER_PREFERENCE_QUERY_ID);
        addFilterPreference(query, Constants.FILTER_PREFERENCE_QUERY_ID);
        submitRequest(query, currentRequest.getPerspective(), currentRequest.getLanguage(),
                currentRequest.getSortBy(), currentRequest.getCategory());
    }

    public void changePerspective(String perspective) {
        removeFilterPreferences(Constants.FILTER_PREFERENCE_SOURCE_ID);
        addFilterPreference(perspective, Constants.FILTER_PREFERENCE_SOURCE_ID);
        submitRequest(currentRequest.getQuery(), perspective, currentRequest.getLanguage(),
                currentRequest.getSortBy(), "");
    }

    public void changeLanguage(String language){
        removeFilterPreferences(Constants.FILTER_PREFERENCE_LANGUAGE_ID);
        addFilterPreference(language, Constants.FILTER_PREFERENCE_LANGUAGE_ID);
        submitRequest(currentRequest.getQuery(), currentRequest.getPerspective(), language,
                currentRequest.getSortBy(), currentRequest.getCategory());
    }

    public void changeSortBy(String sortBy){
        removeFilterPreferences(Constants.FILTER_PREFERENCE_SORT_BY_ID);
        addFilterPreference(sortBy, Constants.FILTER_PREFERENCE_SORT_BY_ID);
        submitRequest(currentRequest.getQuery(), currentRequest.getPerspective(),
                currentRequest.getLanguage(), sortBy, currentRequest.getCategory());
    }

    public void changeCategory(String category){
        removeFilterPreferences(Constants.FILTER_PREFERENCE_CATEGORY_ID);
        addFilterPreference(category, Constants.FILTER_PREFERENCE_CATEGORY_ID);
        submitRequest(currentRequest.getQuery(), "", "",
                currentRequest.getSortBy(), category);
    }

    private void submitRequest(String query, String perspective, String language, String sortBy,
                               String category) {
        Request request = new Request(query, perspective, language, sortBy, category);
        updateNews(request, recyclerView, newsType);
        currentRequest = request;
    }

    private void addFilterPreferences() {
        addFilterPreference(currentRequest.getQuery(), Constants.FILTER_PREFERENCE_QUERY_ID);
        addFilterPreference(currentRequest.getPerspective(), Constants.FILTER_PREFERENCE_SOURCE_ID);
        addFilterPreference(currentRequest.getLanguage(), Constants.FILTER_PREFERENCE_LANGUAGE_ID);
        addFilterPreference(currentRequest.getSortBy(), Constants.FILTER_PREFERENCE_SORT_BY_ID);
        addFilterPreference(currentRequest.getCategory(), Constants.FILTER_PREFERENCE_CATEGORY_ID);

    }

    private void removeFilterPreferences(int type){
        ViewGroup filterSelectionViewGroup = filterSelection;

        for(int i = 0; i < filterSelectionViewGroup.getChildCount(); i++){
            if(filterSelectionViewGroup.getChildAt(i) instanceof  FilterPreferenceTextView){
                FilterPreferenceTextView textView = (FilterPreferenceTextView) filterSelectionViewGroup.getChildAt(i);
                if(textView.getType() == type){
                    filterSelectionViewGroup.removeViewAt(i);
                }
            }
        }
    }

    private void addFilterPreference(String preference, int type) {
        if(!TextUtils.isEmpty(preference)) {
            if (filterSelection.getVisibility() == View.GONE)
                filterSelection.setVisibility(View.VISIBLE);

            final FilterPreferenceTextView textView = new FilterPreferenceTextView(getContext(), type);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                textView.setTypeface(getResources().getFont(R.font.ubuntu_r));
            } else {
                textView.setTypeface(ResourcesCompat.getFont(Objects.requireNonNull(getContext()), R.font.ubuntu_r));
            }
            textView.setText(preference);
            textView.setBackground(getResources().getDrawable(R.drawable.preference_button_border));
            Drawable closeIcon = getResources().getDrawable(R.drawable.ic_close_white_24dp);
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, closeIcon, null);
            textView.setPadding(16, 0, 0, 0);
            textView.setCompoundDrawablePadding(16);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (textView.getType()){
                        case Constants.FILTER_PREFERENCE_QUERY_ID:
                            changeQuery("");
                        case Constants.FILTER_PREFERENCE_SOURCE_ID:
                            changePerspective("");
                        case Constants.FILTER_PREFERENCE_LANGUAGE_ID:
                            changeLanguage("");
                        case Constants.FILTER_PREFERENCE_SORT_BY_ID:
                            changeSortBy("");
                        case Constants.FILTER_PREFERENCE_CATEGORY_ID:
                            changeCategory("");
                    }
                }
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    8,
                    getResources().getDisplayMetrics()
            );

            params.setMargins(px, px, 0, px);

            textView.setLayoutParams(params);

            filterSelection.addView(textView);
        }
    }

    public Request getCurrentRequest() {
        return currentRequest;
    }

    public RecyclerView getRecyclerView(){
        return recyclerView;
    }

    private static class FilterPreferenceTextView extends AppCompatTextView {
        private int type;
        public FilterPreferenceTextView(Context context, int type){
            super(context);
            this.type = type;
        }
        public int getType(){
            return type;
        }

    }

}