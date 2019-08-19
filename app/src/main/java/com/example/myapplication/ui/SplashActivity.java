package com.example.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;

import com.example.myapplication.Application;
import com.example.myapplication.Constants;
import com.example.myapplication.R;
import com.example.myapplication.data.callbacks.DataCallBack;
import com.example.myapplication.data.model.DataModel;
import com.example.myapplication.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SplashActivity extends AppCompatActivity {


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().getReference("user").child("article").keepSynced(true);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        ProgressBar progressBar = findViewById(R.id.splash_progress_bar);
        progressBar.setIndeterminate(true);
        Log.d("SplashActivity", "onCreate");
        new Thread(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }).start();
    }

    private void start() {
        Log.d("SplashActivity", "started");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void updateUI(FirebaseUser user){
            if(user == null){
                Log.d("SplashActivity", "there is no user");
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
            }
            else{
                Log.d("SplashActivity", "there is a user");
                Fade fadeOut = new Fade(Fade.MODE_OUT);
                fadeOut.setDuration(2000);
                getWindow().setExitTransition(fadeOut);
                final Intent mainIntent = new Intent(this, MainActivity.class);
                mainIntent.putExtra("userName", user.getDisplayName());
                Application.getRepository().getRecommendedNews(new DataCallBack<List<DataModel>>() {
                    @Override
                    public void onEmit(List<DataModel> data) {

                    }

                    @Override
                    public void onCompleted() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(mainIntent, ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this).toBundle());
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                }, Constants.NewsType.RECCOMENDED, Constants.DEFAULT_REQUEST);
            }
        }
    }
