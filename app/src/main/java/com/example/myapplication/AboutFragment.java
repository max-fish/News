package com.example.myapplication;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;

import com.squareup.picasso.Picasso;


public class AboutFragment extends Fragment {

    private String selectedCountry;
    private String selectedPerspective;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Spinner countrySpinner = view.findViewById(R.id.country_spinner);
        selectedCountry = (String) countrySpinner.getSelectedItem();
        Spinner perspectiveSpinner = view.findViewById(R.id.perspective_spinner);
        selectedPerspective = (String) perspectiveSpinner.getSelectedItem();

    }
}
