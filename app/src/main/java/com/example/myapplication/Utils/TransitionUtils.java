package com.example.myapplication.Utils;

import android.content.Context;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.transition.Fade;
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
        //changeBounds.setDuration(500);
        set.addTransition(changeBounds);

        Transition changeSize = new TextSizeTransition();
        changeSize.addTarget(R.id.title_view);
        //changeSize.setDuration(500);
        set.addTransition(changeSize);

        return set;
    }

    /**
     * Returns a transition that will (1) move the shared element to its correct size
     * and location on screen, (2) gradually increase/decrease the shared element's
     * text size, and (3) gradually alters the shared element's text color through out
     * the transition.
     */
    public static Transition makeSharedElementEnterTransition(Context context) {
        TransitionSet set = new TransitionSet();
        set.setOrdering(TransitionSet.ORDERING_TOGETHER);

//        Transition recolor = new Recolor();
//        recolor.addTarget(R.id.title_view);
//        recolor.addTarget(context.getString(R.string.hello_world));
//        set.addTransition(recolor);
        Transition changeBounds = new ChangeBounds();
        changeBounds.addTarget(R.id.title_view);
        changeBounds.addTarget(context.getString(R.string.fake_news_transition_title));
        set.addTransition(changeBounds);

        Transition textSize = new TextSizeTransition();
        textSize.addTarget(R.id.title_view);
        textSize.addTarget(context.getString(R.string.fake_news_transition_title));
        set.addTransition(textSize);

        return set;
    }

    /**
     * Returns a modified enter transition that excludes the navigation bar and status
     * bar as targets during the animation. This ensures that the navigation bar and
     * status bar won't appear to "blink" as they fade in/out during the transition.
     */
    public static Transition makeEnterTransition() {
        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        return fade;
    }

}