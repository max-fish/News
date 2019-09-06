package com.example.myapplication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.ActivityOptions;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.transition.Slide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Pair;
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
import com.example.myapplication.ui.newListFragment.CircleTransform;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class FakeNewsActivity extends AppCompatActivity {

    private TextInputEditText title;

    private TextInputLayout titleLayout;

    private TextInputEditText description;

    private TextInputLayout descriptionLayout;

    private TextInputEditText content;

    private LinearLayout fakeNewsContainer;

    private static final int LOAD_IMAGE = 0;

    private static final int USE_CAMERA = 1;

    private ImageView uploadedImage;

    private Uri uploadedImageUri;

    private float radius;


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
        titleLayout = findViewById(R.id.title_text_input_layout);
        description = findViewById(R.id.user_input_description);
        descriptionLayout = findViewById(R.id.description_text_input_layout);
        content = findViewById(R.id.user_input_content);
        Button submitButton = findViewById(R.id.submit_button);
        fakeNewsContainer = findViewById(R.id.fake_news_container);

        ImageButton uploadImageButton = findViewById(R.id.upload_image_button);
        uploadedImage = findViewById(R.id.user_image_upload);
        radius = getResources().getDimension(R.dimen.radius_image);
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
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, USE_CAMERA);
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleLayout.setError(null);
                //View view1 = null;
                //boolean isError = false;

                    if (TextUtils.isEmpty(Objects.requireNonNull(Objects.requireNonNull(title).getText()).toString())){
                        titleLayout.setError("Please enter a title");
                        titleLayout.setFocusable(true);
                        //isError = true;
                    }
                    if(TextUtils.isEmpty(Objects.requireNonNull(description.getText()).toString())){
                        descriptionLayout.setError("Please enter a description");
                        descriptionLayout.setFocusable(true);
                        //isError = true;
                    }

//                    if (isError){
//
//                        view1.setFocusable(true);
//                    }

                    else {
                    Intent madeFakeNewsIntent = new Intent(FakeNewsActivity.this, MadeFakeNews.class);
                    madeFakeNewsIntent.putExtra("fakeNewsTitle", title.getText().toString());
                    madeFakeNewsIntent.putExtra("fakeNewsDescription", description.getText().toString());
                    madeFakeNewsIntent.putExtra("fakeNewsContent", content.getText().toString());
                    if(uploadedImageUri != null) {
                        madeFakeNewsIntent.putExtra("fakeNewsImageUri", uploadedImageUri.toString());
                    }
                    Pair<View, String> titlePair = Pair.create((View) title, getString(R.string.fake_news_transition_title));
                    Pair<View, String> descriptionPair = Pair.create((View) description, getString(R.string.fake_news_transition_description));
                    Pair<View, String> contentPair = Pair.create((View) content, getString(R.string.fake_news_transition_content));
                    Pair<View, String> imagePair = Pair.create((View) uploadedImage, getString(R.string.fake_news_transition_image));
                    ActivityOptions options = ActivityOptions
                            .makeSceneTransitionAnimation(FakeNewsActivity.this,
                                    titlePair,
                                    descriptionPair,
                                    contentPair,
                                    imagePair);
                    startActivity(madeFakeNewsIntent, options.toBundle());

                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            Picasso
                    .get()
                    .load(selectedImage)
                    .into(uploadedImage);
            uploadedImageUri = selectedImage;
        }
        if (requestCode == USE_CAMERA && resultCode == RESULT_OK && data != null) {
            Uri takenImage = data.getData();

            Picasso
                    .get()
                    .load(takenImage)
                    .into(uploadedImage);
            uploadedImageUri = takenImage;
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
