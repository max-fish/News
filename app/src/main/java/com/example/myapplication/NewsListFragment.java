package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.myapplication.dummy.DummyContent;
import com.example.myapplication.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsitem_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }

            final List<DataModel> dataModels = new ArrayList<>();
//            dataModels.add(new DataModel("Title", "DEscription",
//                    "https://cdn-images-1.medium.com/max/1200/0*4ty0Adbdg4dsVBo3.png"));
//            dataModels.add(new DataModel("Title", "DEscription", "http://i.imgur.com/DvpvklR.png"));
//            dataModels.add(new DataModel("Title", "DEscription", "https://cdn-images-1.medium.com/max/1200/0*4ty0Adbdg4dsVBo3.png"));
//            dataModels.add(new DataModel("Title", "DEscription", "https://cdn-images-1.medium.com/max/1200/0*4ty0Adbdg4dsVBo3.png"));
//            dataModels.add(new DataModel("Title", "DEscription", "https://cdn-images-1.medium.com/max/1200/0*4ty0Adbdg4dsVBo3.png"));
//            dataModels.add(new DataModel("Title", "DEscription", "http://i.imgur.com/DvpvklR.png"));


            Call<DataModelCall> repos = Retro.getService().listRepos("bitcoin", "2019-06-16",
                    "publishedAt", "bbc-news", "47075dc90ef54c6f8a0880b20a3ceffc");
            repos.enqueue(new Callback<DataModelCall>() {
                @Override
                public void onResponse(Call<DataModelCall> call, Response<DataModelCall> response) {
                    if(response.body() != null) {
                        recyclerView.setAdapter(new MyNewsItemRecyclerViewAdapter(response.body().getArticles(),getActivity()));
                    }
                }

                @Override
                public void onFailure(Call<DataModelCall> call, Throwable t) {

                }
            });
        }
        return view;
    }


}
