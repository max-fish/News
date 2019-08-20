package com.example.myapplication.ui.preferences;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Application;
import com.example.myapplication.R;
import com.example.myapplication.ui.newListFragment.MyNewsItemRecyclerViewAdapter;

import static com.example.myapplication.Constants.RequestDefinitions.*;
import static com.example.myapplication.Constants.FilterPreferenceIDs.*;

public class TopHeadlinesPreferenceDialogFragment extends PreferenceDialogFragment
implements View.OnClickListener
{
    private String originalCategory;

    private Button business;
    private Button entertainment;
    private Button general;
    private Button health;
    private Button science;
    private Button sports;
    private Button technology;
    public static TopHeadlinesPreferenceDialogFragment newInstance() {
         return new TopHeadlinesPreferenceDialogFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        originalCategory = Application.getRepository().getCurrentRequest().getCategory();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        business = view.findViewById(R.id.category_business);
        business.setOnClickListener(this);
        entertainment = view.findViewById(R.id.category_entertainment);
        entertainment.setOnClickListener(this);
        general = view.findViewById(R.id.category_general);
        general.setOnClickListener(this);
        health = view.findViewById(R.id.category_health);
        health.setOnClickListener(this);
        science = view.findViewById(R.id.category_science);
        science.setOnClickListener(this);
        sports = view.findViewById(R.id.category_sports);
        sports.setOnClickListener(this);
        technology = view.findViewById(R.id.category_technology);
        technology.setOnClickListener(this);

        initTypeBtn(originalCategory);
        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preferences_selection, parent, false);
        if(view instanceof TableLayout){
            TableLayout tableLayout = (TableLayout) view;
            TableRow categoryRow = new TableRow(getContext());
            categoryRow.addView(inflater.inflate(R.layout.category_selection, parent, false));
            tableLayout.addView(categoryRow);
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        String category = originalCategory;
        switch(view.getId()){
            case R.id.category_business:
                category = BUSINESS_CATEGORY;
                clearOriginalSource();
                break;
            case R.id.category_entertainment:
                category = ENTERTAINMENT_CATEGORY;
                clearOriginalSource();
                break;
            case R.id.category_general:
                category = GENERAL_CATEGORY;
                clearOriginalSource();
                break;
            case R.id.category_health:
                category = HEALTH_CATEGORY;
                clearOriginalSource();
                break;
            case R.id.category_science:
                category = SCIENCE_CATEGORY;
                clearOriginalSource();
                break;
            case R.id.category_sports:
                category = SPORTS_CATEGORY;
                clearOriginalSource();
                break;
            case R.id.category_technology:
                category = TECHNOLOGY_CATEGORY;
                clearOriginalSource();
                break;
        }
        initTypeBtn(category);
        if(!category.equals(originalCategory)){
            setCategoryToListFragment(category);
            originalCategory = category;
            disableLanguageButtons();
        }
        super.onClick(view);
    }
    private void initTypeBtn(String originalCategory) {
        if(originalCategory == null){
            return;
        }

        business.setSelected(originalCategory.equals(BUSINESS_CATEGORY));
        entertainment.setSelected(originalCategory.equals(ENTERTAINMENT_CATEGORY));
        general.setSelected(originalCategory.equals(GENERAL_CATEGORY));
        health.setSelected(originalCategory.equals(HEALTH_CATEGORY));
        science.setSelected(originalCategory.equals(SCIENCE_CATEGORY));
        sports.setSelected(originalCategory.equals(SPORTS_CATEGORY));
        technology.setSelected(originalCategory.equals(TECHNOLOGY_CATEGORY));

    }

    private void setCategoryToListFragment(String category) {
        RecyclerView.Adapter adapter = getOriginalFragment().getRecyclerView().getAdapter();
        if (adapter != null)
            ((MyNewsItemRecyclerViewAdapter) adapter).deleteAllItems();
        Application.getRepository().changeCategory(getOriginalFragment(), newsType, category);
        preferencesView.removeFilterPreference(FILTER_PREFERENCE_SOURCE_ID);
        preferencesView.removeFilterPreference(FILTER_PREFERENCE_LANGUAGE_ID);
        preferencesView.removeFilterPreference(FILTER_PREFERENCE_CATEGORY_ID);
        preferencesView.addFilterPreference(getOriginalFragment(), newsType, category, FILTER_PREFERENCE_CATEGORY_ID);
    }

    void clearCategoryOptions(){
        originalCategory = "";
        initTypeBtn(originalCategory);
    }
}
