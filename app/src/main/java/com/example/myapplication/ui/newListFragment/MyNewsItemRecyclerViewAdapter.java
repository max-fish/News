package com.example.myapplication.ui.newListFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.model.DataModel;
import com.example.myapplication.R;
import com.example.myapplication.ui.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MyNewsItemRecyclerViewAdapter extends RecyclerView.Adapter<MyNewsItemRecyclerViewAdapter.ViewHolder> {

    private List<DataModel> mValues;

    private Activity activity;

    private RecyclerView recyclerView;

    public MyNewsItemRecyclerViewAdapter(List<DataModel> items, Activity activity) {
        mValues = items;
        this.activity = activity;
        recyclerView = activity.findViewById(R.id.list);
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

    private View.OnClickListener makeViewHolderOnClickListener(final DataModel item) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            Intent detailsIntent = new Intent(activity, DetailActivity.class);
//            Bundle bundle = new Bundle();
//            fillBundle(bundle, item);
//            detailsIntent.putExtra("info", bundle);
//            activity.startActivity(detailsIntent);.
                deleteAllItems();
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


    int animPos =0;
    int delPos =0;
    final int during = 500;

    private void deleteItem(View rowView, final int position) {

        Animation anim = AnimationUtils.loadAnimation(activity,
                android.R.anim.slide_out_right);
        anim.setDuration(during);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (delPos == 5) {
                    int size = mValues.size();
                    for(int i = 0; i < size; i++){
                        mValues.remove(0);
                    }
                    //notifyItemRangeRemoved(0, size);
//                    mValues = null;
//                    notifyDataSetChanged();
//                    return;
                }
//                mValues.remove(0); //Remove the current content from the array
                delPos++;
//                notifyDataSetChanged(); //Refresh list
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rowView.startAnimation(anim);
    }

    boolean mStopHandler = false;

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
                    if (viewHolder != null){
                        v = viewHolder.itemView;
                        deleteItem(v,animPos );
                        animPos++;
                    }

                } else {
                    handler.removeCallbacksAndMessages(null);
                }

                handler.postDelayed(this, 100);
            }
        };
        activity.runOnUiThread(runnable);
    }

    @Override
    public int getItemCount() {
        return mValues != null ? mValues.size() : 0;
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
