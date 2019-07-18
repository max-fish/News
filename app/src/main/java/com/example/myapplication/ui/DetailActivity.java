package com.example.myapplication.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private TextView title;
    private TextView description;
    private TextView source;
    private TextView content;
    private ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title = findViewById(R.id.detail_title);
        description = findViewById(R.id.detail_description);
        source = findViewById(R.id.detail_source);
        content = findViewById(R.id.detail_content);
        image = findViewById(R.id.detail_image);

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

    private void getBundleDataOut(Bundle bundle) {
        title.setText(bundle.getString("title"));
        description.setText(bundle.getString("description"));
        source.setText(bundle.getString("source"));
        content.setText(bundle.getString("content"));
        Picasso
                .get()
                .load(bundle.getString("urlToImage"))
                .into(image);
    }
}
