package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ui.newListFragment.MyNewsItemRecyclerViewAdapter;

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
        originalCategory = getOriginalFragment().getCurrentRequest().getCategory();
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
        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preferences_selection, parent, false);
        if(view instanceof TableLayout){
            Log.d("TopHeadlines", "Tablelayout!");
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
                category = Constants.BUSINESS_CATEGORY;
                setOriginalSource();
                break;
            case R.id.category_entertainment:
                category = Constants.ENTERTAINMENT_CATEGORY;
                setOriginalSource();
                break;
            case R.id.category_general:
                category = Constants.GENERAL_CATEGORY;
                setOriginalSource();
                break;
            case R.id.category_health:
                category = Constants.HEALTH_CATEGORY;
                setOriginalSource();
                break;
            case R.id.category_science:
                category = Constants.SCIENCE_CATEGORY;
                setOriginalSource();
                break;
            case R.id.category_sports:
                category = Constants.SPORTS_CATEGORY;
                setOriginalSource();
                break;
            case R.id.category_technology:
                category = Constants.TECHNOLOGY_CATEGORY;
                setOriginalSource();
                break;
        }
        initTypeBtn(category);
        if(!category.equals(originalCategory)){
            setCategoryToListFragment(category);
            originalCategory = category;
        }
        super.onClick(view);
    }
    private void initTypeBtn(String originalCategory) {
        if(originalCategory == null){
            return;
        }

        business.setSelected(originalCategory.equals(Constants.BUSINESS_CATEGORY));
        entertainment.setSelected(originalCategory.equals(Constants.ENTERTAINMENT_CATEGORY));
        general.setSelected(originalCategory.equals(Constants.GENERAL_CATEGORY));
        health.setSelected(originalCategory.equals(Constants.HEALTH_CATEGORY));
        science.setSelected(originalCategory.equals(Constants.SCIENCE_CATEGORY));
        sports.setSelected(originalCategory.equals(Constants.SPORTS_CATEGORY));
        technology.setSelected(originalCategory.equals(Constants.TECHNOLOGY_CATEGORY));

    }

    private void setCategoryToListFragment(String category) {
        RecyclerView.Adapter adapter = getOriginalFragment().getRecyclerView().getAdapter();
        if (adapter != null)
            ((MyNewsItemRecyclerViewAdapter) adapter).deleteAllItems();
        getOriginalFragment().changeCategory(category);
    }

    public void clearOptions(){
        originalCategory = "";
        initTypeBtn(originalCategory);
    }
}
