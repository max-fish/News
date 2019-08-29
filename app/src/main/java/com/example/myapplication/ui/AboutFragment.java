package com.example.myapplication.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class AboutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about, container, false);

        ImageButton makeFakeNews = view.findViewById(R.id.make_fake_news_button);
        makeFakeNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent makeFakeNewsIntent = new Intent(getActivity(), FakeNews.class);
                startActivity(makeFakeNewsIntent);
            }
        });

        return view;
    }
}
