package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Application;
import com.example.myapplication.Constants;
import com.example.myapplication.R;
import com.example.myapplication.Utils.WindowUtils;
import com.example.myapplication.ui.newListFragment.NewsListFragment;
import com.example.myapplication.ui.preferences.PreferenceDialogFragment;
import com.example.myapplication.ui.preferences.TopHeadlinesPreferenceDialogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navView;
    private String currentFragmentTag;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.news_recommended:
                    runFragment(getString(R.string.top_headline_fragment_name));
                    return true;
                case R.id.news_all:
                    runFragment(getString(R.string.all_fragment_name));
                    return true;
                case R.id.about_us:
                    runFragment(getString(R.string.about_fragment_name));
                    return true;
            }
            return false;
        }
    };


    private void runFragment(String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment intendedFragment = fragmentManager.findFragmentByTag(tag);
        if(intendedFragment != null){
            fragmentTransaction.hide(Objects.requireNonNull(fragmentManager.findFragmentByTag(currentFragmentTag)));
            fragmentTransaction.show(intendedFragment);
        }
        else{
            if(tag.equals(getString(R.string.top_headline_fragment_name))) {
                fragmentTransaction.add(R.id.fragment_container, NewsListFragment.newInstance(Constants.NewsType.RECOMMENDED), tag);
            }
            else if(tag.equals(getString(R.string.all_fragment_name))){
                fragmentTransaction.add(R.id.fragment_container, NewsListFragment.newInstance(Constants.NewsType.ALL), tag);
            }
            else if(tag.equals(getString(R.string.about_fragment_name))){
                fragmentTransaction.add(R.id.fragment_container, new AboutFragment(), tag);
            }
        }
        currentFragmentTag = tag;
        fragmentTransaction.commit();
    }

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowUtils.setLightStatusBar(getWindow(), this);

        if(getIntent() != null){
            if(!TextUtils.isEmpty(getIntent().getStringExtra("url"))){
                Intent detailIntent = new Intent(this, DetailActivity.class);
                startActivity(detailIntent);
            }
        }

        setContentView(R.layout.activity_main);

        Toolbar preferenceToolbar = findViewById(R.id.preference_toolbar);
        setSupportActionBar(preferenceToolbar);


        fragmentManager = getSupportFragmentManager();

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, NewsListFragment.newInstance(Constants.NewsType.RECOMMENDED), getString(R.string.top_headline_fragment_name));
        currentFragmentTag = getString(R.string.top_headline_fragment_name);
        fragmentTransaction.commit();


    }

    @Override
   public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.preferences, menu);

        SearchView searchItem = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(currentFragmentTag);

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (currentFragment instanceof NewsListFragment) {
                    NewsListFragment currentNewsListFragment = (NewsListFragment) currentFragment;
                    Application.getRepository().changeQuery(currentNewsListFragment,
                            currentNewsListFragment.getNewsType(), query);
                    Log.d("MainActivity", "should change");
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if (currentFragmentTag.equals(getString(R.string.top_headline_fragment_name))) {
            super.onBackPressed();
        } else {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(fragmentManager.findFragmentByTag(currentFragmentTag));
            fragmentTransaction.show(fragmentManager.findFragmentByTag(getString(R.string.top_headline_fragment_name)));
            currentFragmentTag = getString(R.string.top_headline_fragment_name);
            fragmentTransaction.commit();
            navView.setSelectedItemId(R.id.news_recommended);
        }
    }

    public boolean openSettings(MenuItem item) {
        if(item.getItemId() == R.id.action_settings) {
            if (currentFragmentTag.equals(getString(R.string.top_headline_fragment_name))) {
                TopHeadlinesPreferenceDialogFragment topHeadlinesPreferenceDialogFragment =
                        TopHeadlinesPreferenceDialogFragment.newInstance();
                Bundle settingsBundle = new Bundle();
                settingsBundle.putString(getString(R.string.fragment_name_key), currentFragmentTag);
                topHeadlinesPreferenceDialogFragment.setArguments(settingsBundle);
                topHeadlinesPreferenceDialogFragment.show(fragmentManager, getString(R.string.preferences_fragment));
                return true;
            }
            if (currentFragmentTag.equals(getString(R.string.all_fragment_name))) {
                PreferenceDialogFragment preferenceDialogFragment =
                        PreferenceDialogFragment.newInstance();
                Bundle settingsBundle = new Bundle();
                settingsBundle.putString(getString(R.string.fragment_name_key), currentFragmentTag);
                preferenceDialogFragment.setArguments(settingsBundle);
                preferenceDialogFragment.show(fragmentManager, getString(R.string.preferences_fragment));
                return true;
            }
        }
        return false;
    }
}
