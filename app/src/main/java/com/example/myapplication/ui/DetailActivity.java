package com.example.myapplication.ui;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Application;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private TextView title;
    private TextView description;
    private ImageView source;
    private TextView content;
    private ImageView image;
    private ImageView link;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.END);
        getWindow().setEnterTransition(slide);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        title = findViewById(R.id.detail_title);
        description = findViewById(R.id.detail_description);
        source = findViewById(R.id.detail_source);
        content = findViewById(R.id.detail_content);
        image = findViewById(R.id.detail_image);
        link = findViewById(R.id.detail_link);

        Bundle inBundle = getIntent().getBundleExtra("info");

        getBundleDataOut(inBundle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent (Intent.ACTION_VIEW, Uri.parse(bundle.getString("url")));
                startActivity(browserIntent);
            }
        });
    }
}
