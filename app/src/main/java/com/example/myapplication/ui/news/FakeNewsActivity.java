package com.example.myapplication.ui.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ActivityOptions;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Environment;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class FakeNewsActivity extends AppCompatActivity {

    private TextInputEditText title;

    private TextInputLayout titleLayout;

    private TextInputEditText description;

    private TextInputLayout descriptionLayout;

    private TextInputEditText content;


    private static final int LOAD_IMAGE = 0;

    private static final int USE_CAMERA = 1;

    private ImageView uploadedImage;

    private Uri uploadedImageUri;

    private float radius;

    String currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_news);

        Slide slideIn = new Slide();
        slideIn.setSlideEdge(Gravity.END);
        getWindow().setEnterTransition(slideIn);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        title = findViewById(R.id.title_view);
        titleLayout = findViewById(R.id.title_text_input_layout);
        description = findViewById(R.id.user_input_description);
        descriptionLayout = findViewById(R.id.description_text_input_layout);
        content = findViewById(R.id.user_input_content);
        Button submitButton = findViewById(R.id.submit_button);

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

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            useCameraButton.setClickable(false);
        } else {
            useCameraButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        dispatchTakePictureIntent();
                    }
                }
            });
        }


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleLayout.setError(null);

                if (TextUtils.isEmpty(Objects.requireNonNull(Objects.requireNonNull(title).getText()).toString())) {
                    titleLayout.setError("Please enter a title");
                    titleLayout.setFocusable(true);
                }
                if (TextUtils.isEmpty(Objects.requireNonNull(description.getText()).toString())) {
                    descriptionLayout.setError("Please enter a description");
                    descriptionLayout.setFocusable(true);
                } else {
                    Intent madeFakeNewsIntent = new Intent(FakeNewsActivity.this, MadeFakeNews.class);
                    madeFakeNewsIntent.putExtra("fakeNewsTitle", title.getText().toString());
                    madeFakeNewsIntent.putExtra("fakeNewsDescription", description.getText().toString());
                    madeFakeNewsIntent.putExtra("fakeNewsContent", content.getText().toString());
                    madeFakeNewsIntent.putExtra("fakeNewsTitleSize", title.getTextSize());
                    if (!TextUtils.isEmpty(currentPhotoPath)) {
                        madeFakeNewsIntent.putExtra("fakeNewsImageUri", currentPhotoPath);
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
        if (requestCode == USE_CAMERA && resultCode == RESULT_OK) {
            setPic();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.myapplication.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, USE_CAMERA);
            }
        }
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = uploadedImage.getWidth();
        int targetH = uploadedImage.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        uploadedImage.setImageBitmap(bitmap);
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
