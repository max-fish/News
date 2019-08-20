package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.myapplication.LocationFinder;
import com.example.myapplication.ui.newListFragment.NewsListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    private FragmentManager fragmentManager;
    private BottomNavigationView navView;
    private ConstraintLayout welcomeTextContainer;
    private TextView welcomeTextTitle;
    private TextView welcomeTextName;
    private ConstraintLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //LocationFinder.getLocation(this);

        Toolbar preferenceToolbar = findViewById(R.id.preference_toolbar);
        setSupportActionBar(preferenceToolbar);

        fragmentManager = getSupportFragmentManager();

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mainLayout = findViewById(R.id.main_layout);

        welcomeTextContainer = findViewById(R.id.welcome_text_container);
        welcomeTextTitle = findViewById(R.id.welcome_text_title);
        welcomeTextName = findViewById(R.id.welcome_text_name);

        welcomeTextName.setText(getIntent().getStringExtra("userName"));

        final Animation fadeInAnimationTitle = new AlphaAnimation(0,1);
        final Animation fadeInAnimationName = new AlphaAnimation(0,1);
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
            String fragmentTag =  findFirstFragmentOfStack(getSupportFragmentManager()).getName();
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(currentFragment instanceof NewsListFragment){
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == LocationFinder.LOCATION_REQUEST_CODE && resultCode == RESULT_OK){
         }
    }
}
