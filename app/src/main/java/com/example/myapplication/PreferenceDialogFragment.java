package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.core.app.FrameMetricsAggregator;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;


import android.util.EventLog;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.myapplication.ui.newListFragment.NewsListFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PreferenceDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreferenceDialogFragment extends DialogFragment {
    private String originalFragmentTag;

    public PreferenceDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
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
        originalFragmentTag = getArguments().getString("fragmentName");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.preferences_selection, container, false);

        NewsListFragment originalFragment = (NewsListFragment) getFragmentManager().findFragmentByTag(originalFragmentTag);

        ImageButton cnnButton = view.findViewById(R.id.source_cnn);
        ImageButton bbcButton = view.findViewById(R.id.source_bbc);
        ImageButton foxButton = view.findViewById(R.id.source_fox);
        ImageButton msnbcButton = view.findViewById(R.id.source_msnbc);

        String originalSource = originalFragment.getCurrentRequest().getPerspective();
        switch(originalSource) {
            case Constants.CNN_SOURCE:
                cnnButton.setSelected(true);
                break;
            case Constants.BBC_SOURCE:
                bbcButton.setSelected(true);
                break;
            case Constants.FOX_SOURCE:
                foxButton.setSelected(true);
                break;
            case Constants.MSNBC_SOURCE:
                msnbcButton.setSelected(true);
                break;
        }

        setUpOnClick(cnnButton);
        setUpOnClick(bbcButton);
        setUpOnClick(foxButton);
        setUpOnClick(msnbcButton);


        return view;
    }


    private void setUpOnClick(final ImageButton button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleChangedPreference(button);
            }
        });
    }
    private void handleChangedPreference(ImageButton button){
        new EventHandler(getContext(), (NewsListFragment) getFragmentManager().findFragmentByTag("recommended")).handleEvent(button);
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
            if(button.isSelected()){
                button.setSelected(false);
            }
            else{
                button.setSelected(true);
            }
            if (button.getContentDescription().equals(SOURCE_BUTTON_TYPE)) {
                handleSourceButtonEvent(button);
            } else if (button.getContentDescription().equals(LANGUAGE_BUTTON_TYPE)) {
                handleLanguageButtonEvent(button);
            } else if (button.getContentDescription().equals(SORTING_BUTTON_TYPE)) {
                handleSortingButtonEvent(button);
            }
        }

        private void handleSourceButtonEvent(ImageButton button) {
            switch(button.getId()) {
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
