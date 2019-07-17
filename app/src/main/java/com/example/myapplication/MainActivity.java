package com.example.myapplication;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentManager = getSupportFragmentManager();

            switch (item.getItemId()) {
                case R.id.news_recommended:
                    runFragment( "recommended", NewsListFragment.newInstance(1));
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

    private void runFragment(String tag, Fragment fragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragmentManager.findFragmentByTag(tag) == null){
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();
        }else {
            fragmentManager.popBackStack(tag,0);
        }
    }
    private FragmentManager fragmentManager;

    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, NewsListFragment.newInstance(1));
        fragmentTransaction.addToBackStack("recommended");
        fragmentTransaction.commit();
    }

    private void clearStack(FragmentManager fragmentManager){
        for(int i = 0; i < fragmentManager.getBackStackEntryCount(); i++){
            fragmentManager.popBackStack();
        }
    }

    private FragmentManager.BackStackEntry findFirstFragmentOfStack(FragmentManager fragmentManager){
        return fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
    }

    @Override
    public void onBackPressed() {
        if (findFirstFragmentOfStack(fragmentManager).getName().equals("recommended")) {
            Log.d("home", "home");
            //clearStack(fragmentManager);
            super.onBackPressed();
        } else {
            Log.d("not home", "not home");
            clearStack(fragmentManager);
            navView.setSelectedItemId(R.id.news_recommended);
        }
    }
}
