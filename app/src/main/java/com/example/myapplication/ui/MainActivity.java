package com.example.myapplication.ui;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Application;
import com.example.myapplication.Constants;
import com.example.myapplication.R;
import com.example.myapplication.ui.newListFragment.NewsListFragment;
import com.example.myapplication.ui.preferences.PreferenceDialogFragment;
import com.example.myapplication.ui.preferences.PreferencesView;
import com.example.myapplication.ui.preferences.TopHeadlinesPreferenceDialogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.news_recommended:
                    runFragment(getString(R.string.top_headline_fragment_name), NewsListFragment.newInstance(Constants.NewsType.RECOMMENDED));
                    return true;
                case R.id.news_all:
                    runFragment(getString(R.string.all_fragment_name), NewsListFragment.newInstance(Constants.NewsType.ALL));
                    return true;
                case R.id.about_us:
                    runFragment(getString(R.string.about_fragment_name), new AboutFragment());
                    return true;
            }
            return false;
        }
    };


    private void runFragment(String tag, Fragment fragment) {
        PreferencesView preferencesView = findViewById(R.id.filter_selection);
        if(preferencesView.getChildCount() > 0){
            preferencesView.removeAllPreferences();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(fragmentExists(tag)){
            Log.d("MainActivity", "fragment exists");
            fragmentTransaction.replace(R.id.fragment_container, Objects.requireNonNull(fragmentManager.findFragmentByTag(tag)),
                    tag);
        }
        else{
            Log.d("MainActivity", "DNE");
            fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
        }
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    private FragmentManager fragmentManager;
    private ConstraintLayout welcomeTextContainer;
    private TextView welcomeTextName;
    private ConstraintLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);

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

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mainLayout = findViewById(R.id.main_layout);

        if(!TextUtils.isEmpty(getIntent().getStringExtra("userName"))) {
            startWelcomeAnimation();
        }

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, NewsListFragment.newInstance(Constants.NewsType.RECOMMENDED), getString(R.string.top_headline_fragment_name));
        fragmentTransaction.addToBackStack(getString(R.string.top_headline_fragment_name));
        fragmentTransaction.commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.preferences, menu);

        SearchView searchItem = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            String fragmentTag = findFirstFragmentOfStack(getSupportFragmentManager()).getName();
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (currentFragment instanceof NewsListFragment) {
                    NewsListFragment currentNewsListFragment = (NewsListFragment) currentFragment;
                    Application.getRepository().changeQuery(currentNewsListFragment,
                            currentNewsListFragment.getNewsType(), query);
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

    private boolean fragmentExists(String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        return (fragmentManager.findFragmentByTag(tag) != null);
    }

    private void clearStack(FragmentManager fragmentManager) {
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }

    private FragmentManager.BackStackEntry findFirstFragmentOfStack(FragmentManager fragmentManager) {
        return fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
    }

    /*@Override
    public void onBackPressed() {
        if (findFirstFragmentOfStack(fragmentManager).getName().equals(getString(R.string.top_headline_fragment_name))) {
            Log.d("MainActivity", "home");
            //clearStack(fragmentManager);
            super.onBackPressed();
        } else {
            Log.d("MainActivity", "not home");
            clearStack(fragmentManager);
            navView.setSelectedItemId(R.id.news_recommended);
        }
    }*/

    public boolean openSettings(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentManager.BackStackEntry fragment = findFirstFragmentOfStack(fragmentManager);
        if (fragment.getName().equals(getString(R.string.top_headline_fragment_name))) {
            TopHeadlinesPreferenceDialogFragment topHeadlinesPreferenceDialogFragment =
                    TopHeadlinesPreferenceDialogFragment.newInstance();
            Bundle settingsBundle = new Bundle();
            settingsBundle.putString(getString(R.string.fragment_name_key), fragment.getName());
            topHeadlinesPreferenceDialogFragment.setArguments(settingsBundle);
            topHeadlinesPreferenceDialogFragment.show(fm, getString(R.string.preferences_fragment));
            return true;
        }
        if (fragment.getName().equals(getString(R.string.all_fragment_name))) {
            PreferenceDialogFragment preferenceDialogFragment =
                    PreferenceDialogFragment.newInstance();
            Bundle settingsBundle = new Bundle();
            settingsBundle.putString(getString(R.string.fragment_name_key), fragment.getName());
            preferenceDialogFragment.setArguments(settingsBundle);
            preferenceDialogFragment.show(fm, getString(R.string.preferences_fragment));
            return true;
        }
        return false;
    }

    private void startWelcomeAnimation() {
        welcomeTextContainer = findViewById(R.id.welcome_text_container);
        welcomeTextContainer.setVisibility(View.VISIBLE);
        TextView welcomeTextTitle = findViewById(R.id.welcome_text_title);
        welcomeTextName = findViewById(R.id.welcome_text_name);

        welcomeTextName.setText(getIntent().getStringExtra("userName"));

        final Animation fadeInAnimationTitle = new AlphaAnimation(0, 1);
        final Animation fadeInAnimationName = new AlphaAnimation(0, 1);
        final Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);

        fadeInAnimationTitle.setStartOffset(1000);
        fadeInAnimationTitle.setDuration(1000);
        fadeInAnimationName.setDuration(1000);
        fadeOutAnimation.setDuration(1000);

        fadeInAnimationTitle.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                welcomeTextName.setVisibility(View.VISIBLE);
                welcomeTextName.startAnimation(fadeInAnimationName);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fadeInAnimationName.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                welcomeTextContainer.startAnimation(fadeOutAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mainLayout.removeView(welcomeTextContainer);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        welcomeTextTitle.setAnimation(fadeInAnimationTitle);
    }
}
