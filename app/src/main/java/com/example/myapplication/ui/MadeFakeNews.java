package com.example.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;

public class MadeFakeNews extends AppCompatActivity {

    TextInputEditText title;

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_made_fake_news);

        title = findViewById(R.id.finished_fake_news_title);
        title.setText(getIntent().getStringExtra("fakeNewsTitle"));
//        Animation animation = new ScaleAnimation(
//                0.4f, 1f,
//                0.4f, 1f,
//                Animation.RELATIVE_TO_SELF, 0f,
//                Animation.RELATIVE_TO_SELF, 0f);
//        animation.setDuration(1000);
//        animation.setFillBefore(true);
//        title.startAnimation(animation);

        TextInputEditText description = findViewById(R.id.finished_fake_news_description);
        description.setText(getIntent().getStringExtra("fakeNewsDescription"));

        TextInputEditText content = findViewById(R.id.finished_fake_news_Content);
        content.setText(getIntent().getStringExtra("fakeNewsContent"));

        image = findViewById(R.id.finished_fake_news_image);

        String imageUriString = getIntent().getStringExtra("fakeNewsImageUri");

        if (!TextUtils.isEmpty(imageUriString)) {
            setPic(imageUriString);
        }
    }

    private void setPic(String currentPhotoPath) {
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        image.setImageBitmap(bitmap);
    }
}

