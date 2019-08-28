package com.example.myapplication.ui;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class AboutFragment extends Fragment {

    private static final int LOAD_IMAGE = 0;

    private static final int USE_CAMERA = 1;

    private ImageView uploadedImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button uploadImageButton = view.findViewById(R.id.upload_image_button);
        uploadedImage = view.findViewById(R.id.user_image_upload);
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, LOAD_IMAGE);
            }
        });
        Button useCameraButton = view.findViewById(R.id.user_camera_button);
        useCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePictureIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null){
                    startActivityForResult(takePictureIntent, USE_CAMERA);
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

        }
        if(requestCode == USE_CAMERA && resultCode == RESULT_OK && data != null){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) Objects.requireNonNull(extras).get("data");
            uploadedImage.setImageBitmap(imageBitmap);
        }
    }
}
