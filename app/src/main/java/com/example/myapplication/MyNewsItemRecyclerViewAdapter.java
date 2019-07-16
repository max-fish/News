package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class MyNewsItemRecyclerViewAdapter extends RecyclerView.Adapter<MyNewsItemRecyclerViewAdapter.ViewHolder> {

    private final List<DataModel> mValues;

    private Activity activity;

    private View.OnClickListener viewHolderOnClickListener;

    public MyNewsItemRecyclerViewAdapter(List<DataModel> items, Activity activity) {
        mValues = items;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_newsitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DataModel mItem = mValues.get(position);
        putImage(mItem.getUrlToImage(), holder.mImageView);
        holder.mTitleView.setText(mItem.getTitle());
        holder.mDescriptionView.setText(mItem.getDescription());
        holder.source = mItem.getSource().getName();


        holder.mItem.setOnClickListener(makeViewHolderOnClickListener(mItem));
    }

    private void putImage(String url, ImageView imageView) {
        if (url == null || url == "") {

        } else {
            Picasso
                    .get()
                    .load(url)
                    .transform(new CircleTransform(80, 0))
                    .fit()
                    .into(imageView);
        }
    }

    private View.OnClickListener makeViewHolderOnClickListener(final DataModel item){
        if(viewHolderOnClickListener == null){
            viewHolderOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent detailsIntent = new Intent(activity, DetailActivity.class);
                    Bundle bundle = new Bundle();
                    fillBundle(bundle, item);
                    detailsIntent.putExtra("info", bundle);
                    activity.startActivity(detailsIntent);
                }
            };
            return viewHolderOnClickListener;
        }
        else{
            return viewHolderOnClickListener;
        }
    }

    private void fillBundle(Bundle bundle, DataModel item){
        bundle.putString("title", item.getTitle());
        bundle.putString("description", item.getDescription());
        bundle.putString("author", item.getAuthor());
        bundle.putString("content", item.getContent());
        bundle.putString("source", item.getSource().getName());
        bundle.putString("publishedAt",item.getPublishedAt());
        bundle.putString("url", item.getUrl());
        bundle.putString("urlToImage", item.getUrlToImage());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
//    implements View.OnClickListener
    {
        public final TextView mTitleView;
        public final TextView mDescriptionView;
        public final ImageView mImageView;
        public final View mItem;
        public String source;

        public ViewHolder(View view) {
            super(view);
            mItem = view;
            mTitleView = (TextView) view.findViewById(R.id.news_item_title);
            mDescriptionView = (TextView) view.findViewById(R.id.news_item_description);
            mImageView = view.findViewById(R.id.news_item_image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }

}
