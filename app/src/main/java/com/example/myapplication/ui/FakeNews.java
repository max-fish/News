package com.example.myapplication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.transition.Slide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class FakeNews extends AppCompatActivity {

    private EditText title;

    private EditText description;

    private EditText content;

    private LinearLayout fakeNewsContainer;

    private static final int LOAD_IMAGE = 0;

    private static final int USE_CAMERA = 1;

    private ImageView uploadedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Slide slideIn = new Slide();
        slideIn.setSlideEdge(Gravity.END);
        getWindow().setEnterTransition(slideIn);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_news);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    title = findViewById(R.id.user_input_title);
    description = findViewById(R.id.user_input_description);
    content = findViewById(R.id.user_input_content);
        Button submitButton = findViewById(R.id.submit_button);
    fakeNewsContainer = findViewById(R.id.fake_news_container);

    ImageButton uploadImageButton = findViewById(R.id.upload_image_button);
    uploadedImage = findViewById(R.id.user_image_upload);
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, LOAD_IMAGE);
        }
    });
    ImageButton useCameraButton = findViewById(R.id.user_camera_button);
        useCameraButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePictureIntent.resolveActivity(getPackageManager()) != null){
                startActivityForResult(takePictureIntent, USE_CAMERA);
            }
        }
    });

        submitButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(TextUtils.isEmpty(title.getText().toString()) || TextUtils.isEmpty(description.getText().toString())){
                Snackbar.make(findViewById(R.id.fake_news_activity), "Please enter a title and a description", Snackbar.LENGTH_SHORT).show();
            }
            else {
                View fakeNewsView = getLayoutInflater().inflate(R.layout.news_item, fakeNewsContainer, false);
                TextView fakeTitle = fakeNewsView.findViewById(R.id.news_item_title);
                fakeTitle.setText(title.getText().toString());

                TextView fakeDescription = fakeNewsView.findViewById(R.id.news_item_description);
                fakeDescription.setText(description.getText().toString());

                ImageView fakeImage = fakeNewsView.findViewById(R.id.news_item_image);
                fakeImage.setImageDrawable(uploadedImage.getDrawable());
                fakeNewsContainer.addView(fakeNewsView);
            }
        }
    });
}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            uploadedImage.setImageURI(selectedImage);
            uploadedImage.setBackground(null);

        }
        if(requestCode == USE_CAMERA && resultCode == RESULT_OK && data != null){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) Objects.requireNonNull(extras).get("data");
            uploadedImage.setImageBitmap(imageBitmap);
            uploadedImage.setBackground(null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("FakeNews", "saving");
        outState.putString("titleText", title.getText().toString());
        outState.putString("descriptionText", description.getText().toString());
        outState.putString("contentText", content.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        Log.d("FakeNews", "restoring");
        super.onRestoreInstanceState(savedInstanceState);
            title.setText(savedInstanceState.getString("titleText", ""));
            description.setText(savedInstanceState.getString("descriptionText", ""));
            content.setText(savedInstanceState.getString("contentText", ""));

    }
}
