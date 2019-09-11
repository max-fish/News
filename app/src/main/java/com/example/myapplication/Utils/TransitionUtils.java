package com.example.myapplication.Utils;

import android.content.Context;
import android.transition.ArcMotion;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.animation.AnimationUtils;

import com.example.myapplication.R;
import com.example.myapplication.ui.Effects.TextSizeTransition;

public final class TransitionUtils {

    /**
     * Returns a modified enter transition that excludes the navigation bar and status
     * bar as targets during the animation. This ensures that the navigation bar and
     * status bar won't appear to "blink" as they fade in/out during the transition.
     */
//    public static Transition makeEnterTransition() {
//        Transition fade = new Fade();
//        fade.excludeTarget(android.R.id.navigationBarBackground, true);
//        fade.excludeTarget(android.R.id.statusBarBackground, true);
//        return fade;
//    }

    /**
     * Returns a transition that will (1) move the shared element to its correct size
     * and location on screen, (2) gradually increase/decrease the shared element's
     * text size, and (3) gradually alters the shared element's text color through out
     * the transition.
     * @return
     */
    public static android.transition.Transition makeSharedElementEnterTransition() {
        TransitionSet set = new TransitionSet();
        set.setOrdering(TransitionSet.ORDERING_TOGETHER);


        Transition changeBounds = new ChangeBounds();
        changeBounds.addTarget(R.id.title_view);
        changeBounds.addTarget(R.id.user_input_description);
        changeBounds.addTarget(R.id.user_input_content);
        changeBounds.setDuration(500);
        set.addTransition(changeBounds);

        Transition changeSize = new TextSizeTransition();
        changeSize.addTarget(R.id.title_view);
        changeSize.setDuration(500);
        set.addTransition(changeSize);

        return set;
    }


    public static android.transition.Transition makeSharedElementToDetailTransition(Context context){
        TransitionSet set = new TransitionSet();
        set.setOrdering(TransitionSet.ORDERING_TOGETHER);
        set.setDuration(1000);

        ArcMotion arcMotion = new ArcMotion();
        arcMotion.setMinimumHorizontalAngle(15f);
        arcMotion.setMinimumVerticalAngle(0f);
        arcMotion.setMaximumAngle(90);

        Transition changeBounds = new ChangeBounds();
        changeBounds.addTarget(R.id.detail_image);
        changeBounds.setPathMotion(arcMotion);
        changeBounds.setInterpolator(AnimationUtils.loadInterpolator(context, android.R.interpolator.fast_out_slow_in));
        set.addTransition(changeBounds);

        return set;
    }
}