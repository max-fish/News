package com.example.myapplication.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.myapplication.R;
import com.example.myapplication.Utils.WindowUtils;
import com.squareup.picasso.Callback;
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

        WindowUtils.setLightStatusBar(getWindow(), this);


        title = findViewById(R.id.detail_title);
        description = findViewById(R.id.detail_description);
        source = findViewById(R.id.detail_source);
        content = findViewById(R.id.detail_content);
        image = findViewById(R.id.detail_image);
        link = findViewById(R.id.detail_link);


        Bundle inBundle = getIntent().getBundleExtra("info");


        setSupportActionBar((Toolbar) findViewById(R.id.detail_actionbar));

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.expanded_back_button));
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
        title.setText(bundle.getString("title"));
        description.setText(bundle.getString("description"));
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
