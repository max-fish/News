package com.example.myapplication.ui.TextSharedElementTransition;

import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionSet;

import com.example.myapplication.R;

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
        changeBounds.addTarget(R.id.user_input_title);
        //changeBounds.setDuration(500);
        set.addTransition(changeBounds);

        Transition changeSize = new TextSizeTransition();
        changeSize.addTarget(R.id.user_input_title);
        //changeSize.setDuration(500);
        set.addTransition(changeSize);

        return set;
    }
}