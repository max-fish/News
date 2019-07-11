package com.example.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.dummy.DummyContent.DummyItem;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MyNewsItemRecyclerViewAdapter extends RecyclerView.Adapter<MyNewsItemRecyclerViewAdapter.ViewHolder> {

    private final List<DataModel> mValues;

    public MyNewsItemRecyclerViewAdapter(List<DataModel> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_newsitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        DataModel mItem = mValues.get(position);
        putImage(mItem.getUrl(), holder.mImageView);
        holder.mTitleView.setText(mItem.getTitle());
        holder.mDescriptionView.setText(mItem.getDescription());


        holder.mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
            }
        });
    }

    private void putImage(String url, ImageView imageView){
        Log.i("Adapter", "url " + url);
         Picasso
                 .get()
                 .load(url)
                 .placeholder(R.drawable.ic_image_black_24dp)
                 .into(imageView);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTitleView;
        public final TextView mDescriptionView;
        public final ImageView mImageView;
        public final View mItem;

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
