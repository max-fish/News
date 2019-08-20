package com.example.myapplication.ui.newListFragment;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Application;
import com.example.myapplication.data.callbacks.QueryCallBack;
import com.example.myapplication.R;
import com.example.myapplication.data.model.DataModel;
import com.example.myapplication.ui.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MyNewsItemRecyclerViewAdapter extends RecyclerView.Adapter<MyNewsItemRecyclerViewAdapter.ViewHolder> {
    private List<DataModel> mValues;
    private Activity activity;
    private RecyclerView recyclerView;

    private int animPos = 0;
    private int delPos = 0;
    private final int during = 500;
    private boolean mStopHandler = false;

    private int radius;

    MyNewsItemRecyclerViewAdapter(List<DataModel> items, Activity activity, RecyclerView recyclerView) {
        mValues = items;
        this.activity = activity;
        this.recyclerView = recyclerView;
        recyclerView.startLayoutAnimation();

        radius = (int) activity.getResources().getDimension(R.dimen.radius_image);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final DataModel mItem = mValues.get(position);
        putImage(mItem.getUrlToImage(), holder.mImageView);
        holder.mTitleView.setText(mItem.getTitle());
        holder.mDescriptionView.setText(mItem.getDescription());
        holder.mItem.setOnClickListener(makeViewHolderOnClickListener(holder, mItem));
        Application.getRepository().checkArticle(new QueryCallBack() {
            @Override
            public void onFound() {
                holder.mTitleView.setTextColor(Color.GRAY);
                holder.mDescriptionView.setTextColor(Color.GRAY);
                holder.itemView.setBackground(null);
            }
        }, mItem.getUrl());
    }

    private void putImage(String url, ImageView imageView) {
        if (!TextUtils.isEmpty(url)) {
            Picasso
                    .get()
                    .load(url)
                    .transform(new CircleTransform(radius, 0))
                    .resizeDimen(R.dimen.news_item_pic_width, R.dimen.news_item_height)
                    .centerCrop()
                    .into(imageView);
        }
    }

    private View.OnClickListener makeViewHolderOnClickListener(final ViewHolder viewHolder, final DataModel item) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent detailsIntent = new Intent(activity, DetailActivity.class);
            Bundle bundle = new Bundle();
            fillBundle(bundle, item);
            detailsIntent.putExtra("info", bundle);
            activity.startActivity(detailsIntent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
            viewHolder.mTitleView.setTextColor(Color.GRAY);
            viewHolder.mDescriptionView.setTextColor(Color.GRAY);
            viewHolder.itemView.setBackground(null);
            Application.getRepository().saveArticle(item.getUrl());
            }
        };
    }

    private void fillBundle(Bundle bundle, DataModel item) {
        bundle.putString("title", item.getTitle());
        bundle.putString("description", item.getDescription());
        bundle.putString("author", item.getAuthor());
        bundle.putString("content", item.getContent());
        bundle.putString("source", item.getSource().getName());
        bundle.putString("publishedAt", item.getPublishedAt());
        bundle.putString("url", item.getUrl());
        bundle.putString("urlToImage", item.getUrlToImage());
    }


    public void deleteAllItems() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mValues.size() == 0) {
                    mStopHandler = true;
                }
                if (!mStopHandler) {
                    View v;
                    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(animPos);
                    if (viewHolder != null) {
                        v = viewHolder.itemView;
                        animateDeletion(v);
                        animPos++;
                    }
                } else {
                    handler.removeCallbacksAndMessages(null);
                }
                handler.postDelayed(this, during / 5);
            }
        };
        activity.runOnUiThread(runnable);
    }

    private void animateDeletion(final View rowView) {
        Animation anim = AnimationUtils.loadAnimation(activity, android.R.anim.slide_out_right);
        anim.setDuration(during);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rowView.setVisibility(View.INVISIBLE);
                if (delPos++ == 4) {
                    mValues = new ArrayList<>();
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rowView.startAnimation(anim);
    }


    @Override
    public int getItemCount() {
        return mValues != null ? mValues.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
         final TextView mTitleView;
         final TextView mDescriptionView;
         final ImageView mImageView;
         final View mItem;
         ViewHolder(View view) {
            super(view);
            mItem = view;
            mTitleView =  view.findViewById(R.id.news_item_title);
            mDescriptionView = view.findViewById(R.id.news_item_description);
            mImageView = view.findViewById(R.id.news_item_image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }

}
