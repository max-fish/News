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

import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {
    private TextView title;
    private TextView description;
    private TextView source;
    private TextView content;
    private ImageView image;
    private ImageView link;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        title = findViewById(R.id.detail_title);
        description = findViewById(R.id.detail_description);
        source = findViewById(R.id.detail_source);
        content = findViewById(R.id.detail_content);
        image = findViewById(R.id.detail_image);
        link = findViewById(R.id.detail_link);

        getWindow().setEnterTransition(
                TransitionInflater
                        .from(this)
                        .inflateTransition(R.transition.non_shared_to_detail));


        Bundle inBundle = getIntent().getBundleExtra("info");

        if (inBundle != null) {
            getBundleDataOut(inBundle);
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        title.setText(bundle.getString("title"));
        description.setText(bundle.getString("description"));
        content.setText(bundle.getString("content"));
        Picasso
                .get()
                .load(bundle.getString("urlToImage"))
                .into(image);
        source.setText(bundle.getString("source"));
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bundle.getString("url")));
                startActivity(browserIntent);
            }
        });
    }
}
