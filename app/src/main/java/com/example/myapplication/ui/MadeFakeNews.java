package com.example.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SharedElementCallback;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.Utils.TransitionUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class MadeFakeNews extends AppCompatActivity {

    TextInputEditText title;

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_made_fake_news);
        getWindow().setSharedElementEnterTransition(TransitionUtils.makeSharedElementEnterTransition());


        final float startTextSize = getIntent().
                getFloatExtra("fakeNewsTitleSize", -1);

        setEnterSharedElementCallback(
                new SharedElementCallback() {
                    private float textSize = -1;
                    @Override
                    public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                        Log.d("MadeFakeNews", "started");
                        TextView titleTextView = (TextView) sharedElements.get(0);
                        textSize = titleTextView.getTextSize();
                        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, startTextSize);
                    }

                    @Override
                    public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                        if(textSize >=0){
                            TextView titleTextView = (TextView) sharedElements.get(0);
                            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        }
                    }
                }
        );

        title = findViewById(R.id.title_view);
        title.setText(getIntent().getStringExtra("fakeNewsTitle"));


        TextInputEditText description = findViewById(R.id.user_input_description);
        description.setText(getIntent().getStringExtra("fakeNewsDescription"));

        TextInputEditText content = findViewById(R.id.user_input_content);
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

