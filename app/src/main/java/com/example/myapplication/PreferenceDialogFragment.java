package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.myapplication.ui.newListFragment.MyNewsItemRecyclerViewAdapter;
import com.example.myapplication.ui.newListFragment.NewsListFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PreferenceDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreferenceDialogFragment extends DialogFragment implements View.OnClickListener {
    private NewsListFragment originalFragment;
    private ImageButton cnnButton;
    private ImageButton bbcButton;
    private ImageButton foxButton;
    private ImageButton msnbcButton;

    private Button enButton;
    private Button ruButton;
    private Button frButton;
    private Button esButton;

    private Button publishedAtButton;
    private Button relevancyButton;
    private Button popularityButton;


    private String originalSource;
    private String originalLanguage;
    private String originalSortBy;

    public PreferenceDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PreferenceDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreferenceDialogFragment newInstance() {
        PreferenceDialogFragment fragment = new PreferenceDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String originalFragmentTag = getArguments().getString(getString(R.string.fragment_name_key));
        originalFragment = (NewsListFragment) getFragmentManager().findFragmentByTag(originalFragmentTag);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return provideYourFragmentView(inflater, container, savedInstanceState);
    }

    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.preferences_selection, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cnnButton = view.findViewById(R.id.source_cnn);
        cnnButton.setOnClickListener(this);
        bbcButton = view.findViewById(R.id.source_bbc);
        bbcButton.setOnClickListener(this);
        foxButton = view.findViewById(R.id.source_fox);
        foxButton.setOnClickListener(this);
        msnbcButton = view.findViewById(R.id.source_msnbc);
        msnbcButton.setOnClickListener(this);

        enButton = view.findViewById(R.id.language_en);
        enButton.setOnClickListener(this);
        ruButton = view.findViewById(R.id.language_ru);
        ruButton.setOnClickListener(this);
        frButton = view.findViewById(R.id.language_fr);
        frButton.setOnClickListener(this);
        esButton = view.findViewById(R.id.language_es);
        esButton.setOnClickListener(this);

        publishedAtButton = view.findViewById(R.id.sortBy_publishedAt);
        publishedAtButton.setOnClickListener(this);
        relevancyButton = view.findViewById(R.id.sortBy_relevancy);
        relevancyButton.setOnClickListener(this);
        popularityButton = view.findViewById(R.id.sortBy_popularity);
        popularityButton.setOnClickListener(this);

        originalSource = originalFragment.getCurrentRequest().getPerspective();
        originalLanguage = originalFragment.getCurrentRequest().getLanguage();
        originalSortBy = originalFragment.getCurrentRequest().getSortBy();


        initTypeBtn(originalSource, originalLanguage, originalSortBy);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        String source = originalSource;
        String language = originalLanguage;
        String sortBy = originalSortBy;
        switch (view.getId()) {
            case R.id.source_cnn:
                source = Constants.CNN_SOURCE;
                break;
            case R.id.source_bbc:
                source = Constants.BBC_SOURCE;
                break;
            case R.id.source_fox:
                source = Constants.FOX_SOURCE;
                break;
            case R.id.source_msnbc:
                source = Constants.MSNBC_SOURCE;
                break;
            case R.id.language_en:
                language = Constants.EN_LANGUAGE;
                break;
            case R.id.language_ru:
                language = Constants.RU_LANGUAGE;
                break;
            case R.id.language_fr:
                language = Constants.FR_LANGUAGE;
                break;
            case R.id.language_es:
                language = Constants.ES_LANGUAGE;
                break;
            case R.id.sortBy_publishedAt:
                sortBy = Constants.PUBLISHED_AT_SORT;
                break;
            case R.id.sortBy_relevancy:
                sortBy = Constants.RELEVANCY_SORT;
                break;
            case R.id.sortBy_popularity:
                sortBy = Constants.POPULARITY_SORT;
                break;
        }
        initTypeBtn(source, language, sortBy);
        if (!originalSource.equals(source)) {
            setSourceToListFragment(source);
            originalSource = source;
            if (this instanceof TopHeadlinesPreferenceDialogFragment) {
                Log.d("PreferenceDialog", "Top");
                TopHeadlinesPreferenceDialogFragment top = (TopHeadlinesPreferenceDialogFragment) this;
                top.clearOptions();
            }
        }
        if (!originalLanguage.equals(language)) {
            setLanguageToListFragment(language);
            originalLanguage = language;
        }
        if (!originalSortBy.equals(sortBy)) {
            setSortByToListFragment(sortBy);
            originalSortBy = sortBy;
        }
    }

    protected void initTypeBtn(String originalSource, String originalLanguage, String originalSortBy) {
        if (originalSource == null)
            return;

        if (originalLanguage == null)
            return;

        if (originalSortBy == null)
            return;

        cnnButton.setSelected(originalSource.equals(Constants.CNN_SOURCE));
        bbcButton.setSelected(originalSource.equals(Constants.BBC_SOURCE));
        foxButton.setSelected(originalSource.equals(Constants.FOX_SOURCE));
        msnbcButton.setSelected(originalSource.equals(Constants.MSNBC_SOURCE));

        enButton.setSelected(originalLanguage.equals(Constants.EN_LANGUAGE));
        ruButton.setSelected(originalLanguage.equals(Constants.RU_LANGUAGE));
        frButton.setSelected(originalLanguage.equals(Constants.FR_LANGUAGE));
        esButton.setSelected(originalLanguage.equals(Constants.ES_LANGUAGE));

        publishedAtButton.setSelected(originalSortBy.equals(Constants.PUBLISHED_AT_SORT));
        relevancyButton.setSelected(originalSortBy.equals(Constants.RELEVANCY_SORT));
        popularityButton.setSelected(originalSortBy.equals(Constants.POPULARITY_SORT));
    }

    private void setSourceToListFragment(String source) {
        RecyclerView.Adapter adapter = originalFragment.getRecyclerView().getAdapter();
        if (adapter != null)
            ((MyNewsItemRecyclerViewAdapter) adapter).deleteAllItems();
        if (originalFragment != null)
            originalFragment.changePerspective(source);
    }

    private void setLanguageToListFragment(String language) {
        RecyclerView.Adapter adapter = originalFragment.getRecyclerView().getAdapter();
        if (adapter != null)
            ((MyNewsItemRecyclerViewAdapter) adapter).deleteAllItems();
        if (originalFragment != null) {
            originalFragment.changeLanguage(language);
        }
    }

    private void setSortByToListFragment(String sortBy) {
        RecyclerView.Adapter adapter = originalFragment.getRecyclerView().getAdapter();
        if (adapter != null)
            ((MyNewsItemRecyclerViewAdapter) adapter).deleteAllItems();
        if (originalFragment != null) {
            originalFragment.changeSortBy(sortBy);
        }
    }

    NewsListFragment getOriginalFragment() {
        return originalFragment;
    }

    void setOriginalSource() {
        this.originalSource = "";
    }
}