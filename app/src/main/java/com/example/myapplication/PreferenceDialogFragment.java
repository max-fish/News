package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PreferenceDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreferenceDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match

    public PreferenceDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment PreferenceDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreferenceDialogFragment newInstance() {
        PreferenceDialogFragment fragment = new PreferenceDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.preferences_selection, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button cnnButton = view.findViewById(R.id.source_cnn);
        cnnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean selected = cnnButton.isSelected();
                if(selected){
                    cnnButton.setSelected(false);
                }
                else{
                    cnnButton.setSelected(true);
                }
            }
        });
    }
}
