package com.example.myapplication.ui.preferences;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.example.myapplication.Application;
import com.example.myapplication.Constants;
import com.example.myapplication.R;
import com.example.myapplication.data.callbacks.DataCallBack;
import com.example.myapplication.data.model.DataModel;
import java.util.List;
import java.util.Objects;

import static com.example.myapplication.Constants.FilterPreferenceIDs.*;

public class PreferencesView extends LinearLayout {

    public PreferencesView(Context context){
        super(context);
    }

    public PreferencesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PreferencesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addFilterPreference(final DataCallBack<List<DataModel>> callBack, final Constants.NewsType newsType, String preference, int type) {
        Log.d("NewsListFragment", "adding preference" + preference);
        if(!TextUtils.isEmpty(preference)) {
            if (this.getVisibility() == View.GONE)
                this.setVisibility(View.VISIBLE);

            final FilterPreferenceTextView textView = new FilterPreferenceTextView(getContext(), type);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                textView.setTypeface(getResources().getFont(R.font.ubuntu_r));
            } else {
                textView.setTypeface(ResourcesCompat.getFont(Objects.requireNonNull(getContext()), R.font.ubuntu_r));
            }
            textView.setText(preference);
            textView.setBackground(getResources().getDrawable(R.drawable.preference_button_border));
            Drawable closeIcon = getResources().getDrawable(R.drawable.ic_close_white_24dp);
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, closeIcon, null);
            textView.setPadding(16, 0, 0, 0);
            textView.setCompoundDrawablePadding(16);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final ViewGroup filterSelectionViewGroup = PreferencesView.this;
                    Log.d("NewsListFragment", "delete clicked");
                    switch (textView.getType()){
                        case FILTER_PREFERENCE_QUERY_ID:
                            Application.getRepository().changeQuery(callBack, newsType, "");
                            break;
                        case FILTER_PREFERENCE_SOURCE_ID:
                            Application.getRepository().changeSource(callBack, newsType, "");
                            break;
                        case FILTER_PREFERENCE_LANGUAGE_ID:
                            Application.getRepository().changeSource(callBack, newsType, "");
                            break;
                        case FILTER_PREFERENCE_SORT_BY_ID:
                            Application.getRepository().changeSortBy(callBack, newsType, "");
                            break;
                        case FILTER_PREFERENCE_CATEGORY_ID:
                            Application.getRepository().changeCategory(callBack, newsType, "");
                            break;
                    }

                    Animation anim = new AlphaAnimation(1,0);
                    anim.setDuration(300);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            filterSelectionViewGroup.removeView(view);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    view.startAnimation(anim);
                }

            });

            LayoutParams params = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
            );

            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    8,
                    getResources().getDisplayMetrics()
            );

            params.setMargins(px, px, 0, px);

            textView.setLayoutParams(params);

            this.addView(textView);
        }
    }

    public void removeFilterPreference(int type){
        Log.d("NewsListFragment", "removing view");

        for(int i = 0; i < this.getChildCount(); i++){
            if(this.getChildAt(i) instanceof  FilterPreferenceTextView){
                FilterPreferenceTextView textView = (FilterPreferenceTextView) this.getChildAt(i);
                if(textView.getType() == type){
                    Log.d("NewsListFragment", "deleted");
                    this.removeViewAt(i);
                }
            }
        }
    }

    private static class FilterPreferenceTextView extends AppCompatTextView {
        private int type;
        FilterPreferenceTextView(Context context, int type){
            super(context);
            this.type = type;
        }
        int getType(){
            return type;
        }

    }
}
