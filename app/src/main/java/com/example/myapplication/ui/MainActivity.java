package com.example.myapplication.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.ui.newListFragment.NewsListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.news_recommended:
                    runFragment("recommended", NewsListFragment.newInstance(1));
                    return true;
                case R.id.news_all:
                    runFragment("all", NewsListFragment.newInstance(1));
                    return true;
                case R.id.about_us:
                    runFragment("about", new AboutFragment());
                    return true;
            }
            return false;
        }
    };

    private void runFragment(String tag, Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
        fragmentTransaction.commit();
    }

    private FragmentManager fragmentManager;

    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, NewsListFragment.newInstance(1));
        fragmentTransaction.addToBackStack("recommended");
        fragmentTransaction.commit();
    }

    private void clearStack(FragmentManager fragmentManager) {
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }

    private FragmentManager.BackStackEntry findFirstFragmentOfStack(FragmentManager fragmentManager) {
        return fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
    }

    @Override
    public void onBackPressed() {
        if (findFirstFragmentOfStack(fragmentManager).getName().equals("recommended")) {
            Log.d("MainActivity", "home");
            //clearStack(fragmentManager);
            super.onBackPressed();
        } else {
            Log.d("MainActivity", "not home");
            clearStack(fragmentManager);
            navView.setSelectedItemId(R.id.news_recommended);
        }
    }
}
