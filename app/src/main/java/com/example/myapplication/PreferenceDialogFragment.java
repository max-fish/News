package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


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
    private Preferences currentPreference;
    private NewsListFragment originalFragment;
    private ImageButton cnnButton;
    private ImageButton bbcButton;
    private ImageButton foxButton;
    private ImageButton msnbcButton;
    private String originalSource;

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
        String originalFragmentTag = getArguments().getString("fragmentName");
        originalFragment = (NewsListFragment) getFragmentManager().findFragmentByTag(originalFragmentTag);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.preferences_selection, container, false);
        cnnButton = view.findViewById(R.id.source_cnn);
        cnnButton.setOnClickListener(this);
        bbcButton = view.findViewById(R.id.source_bbc);
        bbcButton.setOnClickListener(this);
        foxButton = view.findViewById(R.id.source_fox);
        foxButton.setOnClickListener(this);
        msnbcButton = view.findViewById(R.id.source_msnbc);
        msnbcButton.setOnClickListener(this);

        Button enButton = view.findViewById(R.id.language_en);
        Button ruButton = view.findViewById(R.id.language_ru);
        Button frButton = view.findViewById(R.id.language_fr);
        Button esButton = view.findViewById(R.id.language_es);


        originalSource = originalFragment.getCurrentRequest().getPerspective();
        initTypeBtn(originalSource);

        String originalLanguage = originalFragment.getCurrentRequest().getLanguage();
        switch (originalLanguage) {
            case Constants.EN_LANGUAGE:
                enButton.setSelected(true);
                break;
            case Constants.RU_LANGUAGE:
                ruButton.setSelected(true);
                break;
            case Constants.FR_LANGUAGE:
                frButton.setSelected(true);
                break;
            case Constants.ES_LANGUAGE:
                esButton.setSelected(true);
                break;
        }

        String originalSorting = originalFragment.getCurrentRequest().getSortBy();
        switch (originalSorting) {
            case Constants.PUBLISHED_AT_SORT:

        }


//        setUpOnClick(cnnButton);
//        setUpOnClick(bbcButton);
//        setUpOnClick(foxButton);
//        setUpOnClick(msnbcButton);

        return view;
    }

    private void initTypeBtn(String originalSource) {
        if (originalSource == null)
            return;

        cnnButton.setSelected(originalSource.equals(Constants.CNN_SOURCE));
        bbcButton.setSelected(originalSource.equals(Constants.BBC_SOURCE));
        foxButton.setSelected(originalSource.equals(Constants.FOX_SOURCE));
        msnbcButton.setSelected(originalSource.equals(Constants.MSNBC_SOURCE));
    }


//    private void setUpOnClick(final ImageButton button) {
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                handleChangedPreference(button);
//            }
//        });
//    }

//    private void handleChangedPreference(ImageButton button) {
//        new EventHandler(getContext(), (NewsListFragment) getFragmentManager().findFragmentByTag("recommended")).handleEvent(button);
//    }

    @Override
    public void onClick(View view) {
        String type = null;
        switch (view.getId()) {
            case R.id.source_cnn:
                type = Constants.CNN_SOURCE;
                break;
            case R.id.source_bbc:
                type = Constants.BBC_SOURCE;
                break;
            case R.id.source_fox:
                type = Constants.FOX_SOURCE;
                break;
            case R.id.source_msnbc:
                type = Constants.MSNBC_SOURCE;
                break;
        }
        initTypeBtn(type);
//        new EventHandler(getContext(), (NewsListFragment) getFragmentManager().findFragmentByTag("recommended")).handleEvent((ImageButton) view);
        if (!originalSource.equals(type)){
            setTypeToListFragment(type);
            originalSource = type;
        }
    }

    private void setTypeToListFragment(String type) {
        RecyclerView.Adapter adapter = originalFragment.getRecyclerView().getAdapter();
        if (adapter != null)
            ((MyNewsItemRecyclerViewAdapter) adapter).deleteAllItems();
        NewsListFragment fragment = (NewsListFragment) getFragmentManager().findFragmentByTag("recommended");
        if (fragment != null)
            fragment.changePerspective(type);
    }


    private class EventHandler {
        private final String SOURCE_BUTTON_TYPE;
        private final String LANGUAGE_BUTTON_TYPE;
        private final String SORTING_BUTTON_TYPE;
        private final NewsListFragment fragment;

        private EventHandler(Context context, NewsListFragment fragment) {
            SOURCE_BUTTON_TYPE = context.getResources().getString(R.string.source_preference_button);
            LANGUAGE_BUTTON_TYPE = context.getResources().getString(R.string.language_preference_button);
            SORTING_BUTTON_TYPE = context.getResources().getString(R.string.sorting_preference_button);
            this.fragment = fragment;
        }

        public void handleEvent(ImageButton button) {
            if (button.isSelected()) {
                button.setSelected(false);
                RecyclerView.Adapter adapter = originalFragment.getRecyclerView().getAdapter();
                if (adapter != null)
                    ((MyNewsItemRecyclerViewAdapter) adapter).deleteAllItems();
            } else {
                button.setSelected(true);
                if (button.getContentDescription().equals(SOURCE_BUTTON_TYPE)) {
                    handleSourceButtonEvent(button);
                } else if (button.getContentDescription().equals(LANGUAGE_BUTTON_TYPE)) {
                    handleLanguageButtonEvent(button);
                } else if (button.getContentDescription().equals(SORTING_BUTTON_TYPE)) {
                    handleSortingButtonEvent(button);
                }
            }
        }

        private void handleSourceButtonEvent(ImageButton button) {

            switch (button.getId()) {
                case R.id.source_cnn:
                    fragment.changePerspective(Constants.CNN_SOURCE);
                    break;
                case R.id.source_bbc:
                    fragment.changePerspective(Constants.BBC_SOURCE);
                    break;
                case R.id.source_fox:
                    fragment.changePerspective(Constants.FOX_SOURCE);
                    break;
                case R.id.source_msnbc:
                    fragment.changePerspective(Constants.MSNBC_SOURCE);
                    break;
            }
        }

        private void handleLanguageButtonEvent(ImageButton button) {

        }

        private void handleSortingButtonEvent(ImageButton button) {

        }

    }
}
