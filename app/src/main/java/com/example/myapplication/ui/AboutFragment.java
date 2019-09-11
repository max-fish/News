package com.example.myapplication.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.ui.Effects.RevealAnimation;

import java.util.Objects;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about, container, false);

        ImageButton makeFakeNews = view.findViewById(R.id.make_fake_news_button);
        makeFakeNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRevealActivity(view);
            }
        });

        return view;
    }

    private void startRevealActivity(View v) {
        //calculates the center of the View v you are passing
        int revealX = (int) (v.getX() + v.getWidth() / 2);
        int revealY = (int) (v.getY() + v.getHeight() / 2);

        //create an intent, that launches the second activity and pass the x and y coordinates
        Intent intent = new Intent(getActivity(), FakeNewsActivity.class);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        //just start the activity as an shared transition, but set the options bundle to null
        ActivityCompat.startActivity(Objects.requireNonNull(getContext()), intent, null);

        //to prevent strange behaviours override the pending transitions
        Objects.requireNonNull(getActivity()).overridePendingTransition(0, 0);
    }
}
