package com.example.myapplication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.transition.Slide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class FakeNewsActivity extends AppCompatActivity {

    private TextInputLayout title;

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

    title = findViewById(R.id.title_text_input_layout);
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
            if(TextUtils.isEmpty(Objects.requireNonNull(title.getEditText()).getText().toString()) || TextUtils.isEmpty(description.getText().toString())){
                Snackbar.make(findViewById(R.id.fake_news_activity), "Please enter a title and a description", Snackbar.LENGTH_SHORT).show();
            }
            else {
                Intent madeFakeNewsIntent = new Intent(FakeNewsActivity.this, MadeFakeNews.class);
                madeFakeNewsIntent.putExtra("fakeNewsTitle", title.getEditText().getText().toString());
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(FakeNewsActivity.this, title, getString(R.string.fake_news_transition_name));
                startActivity(madeFakeNewsIntent, options.toBundle());

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
            BitmapDrawable drawable = (BitmapDrawable) uploadedImage.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            uploadedImage.setImageBitmap(ImageUtils.getRoundedCornerBitmap(bitmap, 80, 80,80,80,80,80,80,80));
        }
        if(requestCode == USE_CAMERA && resultCode == RESULT_OK && data != null){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) Objects.requireNonNull(extras).get("data");
            Bitmap roundedBitmap = ImageUtils.getRoundedCornerBitmap(imageBitmap, 80, 80,80,80,80,80,80,80);
            uploadedImage.setImageBitmap(roundedBitmap);
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
}
