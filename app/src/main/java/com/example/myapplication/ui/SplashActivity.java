package com.example.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);

        Log.d("SplashActivity", "onCreate");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void updateUI(FirebaseUser user) {
        if (user == null) {
            Log.d("SplashActivity", "there is no user");
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else {
            Log.d("SplashActivity", "there is a user");
            Fade fadeOut = new Fade(Fade.MODE_OUT);
            fadeOut.setDuration(2000);
            getWindow().setExitTransition(fadeOut);
            final Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.putExtra("userName", user.getDisplayName());
            startActivity(mainIntent, ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this).toBundle());
        }
        finish();
    }
}
