package com.example.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;

public class MadeFakeNews extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_made_fake_news);

        TextInputEditText title = findViewById(R.id.finished_fake_news_title);
        title.setText(getIntent().getStringExtra("fakeNewsTitle"));

        TextInputEditText description = findViewById(R.id.finished_fake_news_description);
        description.setText(getIntent().getStringExtra("fakeNewsDescription"));

        TextInputEditText content = findViewById(R.id.finished_fake_news_Content);
        content.setText(getIntent().getStringExtra("fakeNewsContent"));

        ImageView image = findViewById(R.id.finished_fake_news_image);
        image.setImageBitmap((Bitmap) getIntent().getParcelableExtra("fakeNewsImage"));
    }
}
