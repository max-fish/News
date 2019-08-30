package com.example.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;

public class MadeFakeNews extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_made_fake_news);
        TextInputEditText title = findViewById(R.id.finished_fake_news_title);
        title.setText(getIntent().getStringExtra("fakeNewsTitle"));
    }
}
