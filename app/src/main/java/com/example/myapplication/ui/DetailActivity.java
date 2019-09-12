package com.example.myapplication.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.transition.Visibility;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.myapplication.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {
    private CollapsingToolbarLayout title;
    private TextView description;
    private TextView source;
    private TextView content;
    private ImageView image;
    private ImageView link;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        title = findViewById(R.id.detail_actionbar);
        description = findViewById(R.id.detail_description);
        source = findViewById(R.id.detail_source);
        content = findViewById(R.id.detail_content);
        image = findViewById(R.id.detail_image);
        link = findViewById(R.id.detail_link);


        Bundle inBundle = getIntent().getBundleExtra("info");

//        setSupportActionBar((Toolbar) findViewById(R.id.detail_actionbar));
//
//
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (inBundle != null) {
            getBundleDataOut(inBundle);
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

    private void getBundleDataOut(final Bundle bundle) {
        title.setTitle((bundle.getString("title")));
        description.setText(bundle.getString("description")+bundle.getString("description")+bundle.getString("description")+bundle.getString("description")+bundle.getString("description")+bundle.getString("description")
                +bundle.getString("description")+bundle.getString("description")+bundle.getString("description")+bundle.getString("description"));
        content.setText(bundle.getString("content"));
        source.setText(bundle.getString("source"));
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bundle.getString("url")));
                startActivity(browserIntent);
            }
        });
        ActivityCompat.postponeEnterTransition(this);
        Picasso
                .get()
                .load(bundle.getString("urlToImage"))
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        ActivityCompat.startPostponedEnterTransition(DetailActivity.this);
                    }

                    @Override
                    public void onError(Exception e) {
                        ActivityCompat.startPostponedEnterTransition(DetailActivity.this);
                    }
                });
    }
}
