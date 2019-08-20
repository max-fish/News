package com.example.myapplication.ui.preferences;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Application;
import com.example.myapplication.Constants;
import com.example.myapplication.R;
import com.example.myapplication.ui.login.LoginActivity;
import com.example.myapplication.ui.newListFragment.CircleTransform;
import com.example.myapplication.ui.newListFragment.MyNewsItemRecyclerViewAdapter;
import com.example.myapplication.ui.newListFragment.NewsListFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static com.example.myapplication.Constants.RequestDefinitions.*;
import static com.example.myapplication.Constants.FilterPreferenceIDs.*;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PreferenceDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreferenceDialogFragment extends DialogFragment implements View.OnClickListener {
    private NewsListFragment originalFragment;

    PreferencesView preferencesView;
    Constants.NewsType newsType;

    private ImageButton cnnButton;
    private ImageButton bbcButton;
    private ImageButton foxButton;
    private ImageButton msnbcButton;

    private Button enButton;
    private Button ruButton;
    private Button frButton;
    private Button esButton;

    private Button publishedAtButton;
    private Button relevancyButton;
    private Button popularityButton;


    private String originalSource;
    private String originalLanguage;
    private String originalSortBy;

    private boolean languageButtonsDisabled;


    public PreferenceDialogFragment() {
        // Required empty public constructor
    }

    public static PreferenceDialogFragment newInstance() {
        return new PreferenceDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String originalFragmentTag = Objects.requireNonNull(getArguments()).getString(getString(R.string.fragment_name_key));
        originalFragment = (NewsListFragment) Objects.requireNonNull(getFragmentManager()).findFragmentByTag(originalFragmentTag);
        newsType = (Constants.NewsType) Objects.requireNonNull(Objects.requireNonNull(originalFragment).getArguments()).get("newsType");
        preferencesView = Objects.requireNonNull(getActivity()).findViewById(R.id.filter_selection);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return provideYourFragmentView(inflater, container, savedInstanceState);
    }

    View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.preferences_selection, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setProfileInfo(view);
        Button signOutButton = view.findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                FirebaseAuth.getInstance().signOut();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(view.getContext(), gso);
                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (Objects.requireNonNull(getActivity()).getIntent().getBooleanExtra("isFromLogin", false)) {
                                    getActivity().finish();
                                    Toast.makeText(view.getContext(), "signed out", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(loginIntent);
                                    getActivity().finish();
                                }
                            }
                        });
            }
        });

        cnnButton = view.findViewById(R.id.source_cnn);
        cnnButton.setOnClickListener(this);
        bbcButton = view.findViewById(R.id.source_bbc);
        bbcButton.setOnClickListener(this);
        foxButton = view.findViewById(R.id.source_fox);
        foxButton.setOnClickListener(this);
        msnbcButton = view.findViewById(R.id.source_msnbc);
        msnbcButton.setOnClickListener(this);

        enButton = view.findViewById(R.id.language_en);
        enButton.setOnClickListener(this);
        ruButton = view.findViewById(R.id.language_ru);
        ruButton.setOnClickListener(this);
        frButton = view.findViewById(R.id.language_fr);
        frButton.setOnClickListener(this);
        esButton = view.findViewById(R.id.language_es);
        esButton.setOnClickListener(this);

        publishedAtButton = view.findViewById(R.id.sortBy_publishedAt);
        publishedAtButton.setOnClickListener(this);
        relevancyButton = view.findViewById(R.id.sortBy_relevancy);
        relevancyButton.setOnClickListener(this);
        popularityButton = view.findViewById(R.id.sortBy_popularity);
        popularityButton.setOnClickListener(this);

        originalSource = Application.getRepository().getCurrentRequest().getSource();
        originalLanguage = Application.getRepository().getCurrentRequest().getLanguage();
        originalSortBy = Application.getRepository().getCurrentRequest().getSortBy();

        initTypeBtn(originalSource, originalLanguage, originalSortBy);

        if (originalLanguage.equals("") && originalSource.equals("")) {
            disableLanguageButtons();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    private void setProfileInfo(@NonNull View view) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && getActivity() != null) {
            ImageView profilePicture = view.findViewById(R.id.profile_picture);
            int radius = (int) getActivity().getResources().getDimension(R.dimen.radius_image);
            TextView profileName = view.findViewById(R.id.profile_name);
            TextView profileEmail = view.findViewById(R.id.profile_email);
            Picasso
                    .get()
                    .load(currentUser.getPhotoUrl())
                    .transform(new CircleTransform(radius, 0))
                    .into(profilePicture);
            profileName.setText(currentUser.getDisplayName());
            profileEmail.setText(currentUser.getEmail());
        }
    }

    @Override
    public void onClick(View view) {
        String source = originalSource;
        String language = originalLanguage;
        String sortBy = originalSortBy;
        switch (view.getId()) {
            case R.id.source_cnn:
                source = CNN_SOURCE;
                break;
            case R.id.source_bbc:
                source = BBC_SOURCE;
                break;
            case R.id.source_fox:
                source = FOX_SOURCE;
                break;
            case R.id.source_msnbc:
                source = MSNBC_SOURCE;
                break;
            case R.id.language_en:
                language = EN_LANGUAGE;
                break;
            case R.id.language_ru:
                language = RU_LANGUAGE;
                break;
            case R.id.language_fr:
                language = FR_LANGUAGE;
                break;
            case R.id.language_es:
                language = ES_LANGUAGE;
                break;
            case R.id.sortBy_publishedAt:
                sortBy = PUBLISHED_AT_SORT;
                break;
            case R.id.sortBy_relevancy:
                sortBy = RELEVANCY_SORT;
                break;
            case R.id.sortBy_popularity:
                sortBy = POPULARITY_SORT;
                break;
        }
        initTypeBtn(source, language, sortBy);
        if (!originalSource.equals(source)) {
            setSourceToListFragment(source);
            originalSource = source;
            if (this instanceof TopHeadlinesPreferenceDialogFragment) {
                Log.d("PreferenceDialog", "Top");
                TopHeadlinesPreferenceDialogFragment top = (TopHeadlinesPreferenceDialogFragment) this;
                top.clearCategoryOptions();
            }
        }
        if (!originalLanguage.equals(language)) {
            setLanguageToListFragment(language);
            originalLanguage = language;
        }
        if (!originalSortBy.equals(sortBy)) {
            setSortByToListFragment(sortBy);
            originalSortBy = sortBy;
        }
    }

    private void initTypeBtn(String originalSource, String originalLanguage, String originalSortBy) {
        if (originalSource == null)
            return;

        if (originalLanguage == null)
            return;

        if (originalSortBy == null)
            return;

        cnnButton.setSelected(originalSource.equals(CNN_SOURCE));
        bbcButton.setSelected(originalSource.equals(BBC_SOURCE));
        foxButton.setSelected(originalSource.equals(FOX_SOURCE));
        msnbcButton.setSelected(originalSource.equals(MSNBC_SOURCE));

        enButton.setSelected(originalLanguage.equals(EN_LANGUAGE));
        ruButton.setSelected(originalLanguage.equals(RU_LANGUAGE));
        frButton.setSelected(originalLanguage.equals(FR_LANGUAGE));
        esButton.setSelected(originalLanguage.equals(ES_LANGUAGE));

        publishedAtButton.setSelected(originalSortBy.equals(PUBLISHED_AT_SORT));
        relevancyButton.setSelected(originalSortBy.equals(RELEVANCY_SORT));
        popularityButton.setSelected(originalSortBy.equals(POPULARITY_SORT));
    }

    private void setSourceToListFragment(String source) {
        RecyclerView.Adapter adapter = originalFragment.getRecyclerView().getAdapter();
        if (adapter != null)
            ((MyNewsItemRecyclerViewAdapter) adapter).deleteAllItems();
        Application.getRepository().changeSource(originalFragment, newsType, source);
        preferencesView.removeFilterPreference(FILTER_PREFERENCE_SOURCE_ID);
        preferencesView.removeFilterPreference(FILTER_PREFERENCE_CATEGORY_ID);
        preferencesView.addFilterPreference
                (originalFragment, newsType, source, FILTER_PREFERENCE_SOURCE_ID);
        if (languageButtonsDisabled)
            enableLanguageButtons();
    }

    private void setLanguageToListFragment(String language) {
        RecyclerView.Adapter adapter = originalFragment.getRecyclerView().getAdapter();
        if (adapter != null)
            ((MyNewsItemRecyclerViewAdapter) adapter).deleteAllItems();
        Application.getRepository().changeLanguage(originalFragment, newsType, language);
        preferencesView.removeFilterPreference(FILTER_PREFERENCE_LANGUAGE_ID);
        preferencesView.addFilterPreference
                (originalFragment, newsType, language, FILTER_PREFERENCE_LANGUAGE_ID);
    }

    private void setSortByToListFragment(String sortBy) {
        RecyclerView.Adapter adapter = originalFragment.getRecyclerView().getAdapter();
        if (adapter != null)
            ((MyNewsItemRecyclerViewAdapter) adapter).deleteAllItems();
        preferencesView.removeFilterPreference(FILTER_PREFERENCE_SORT_BY_ID);
        Application.getRepository().changeSortBy(originalFragment, newsType, sortBy);
        preferencesView.addFilterPreference
                (originalFragment, newsType, sortBy, FILTER_PREFERENCE_SORT_BY_ID);
    }

    NewsListFragment getOriginalFragment() {
        return originalFragment;
    }

    void clearOriginalSource() {
        originalSource = "";
    }

    void disableLanguageButtons() {
        originalLanguage = "";

        enButton.setEnabled(false);
        ruButton.setEnabled(false);
        frButton.setEnabled(false);
        esButton.setEnabled(false);

        languageButtonsDisabled = true;
    }

    private void enableLanguageButtons() {
        Application.getRepository().changeLanguage(originalFragment, newsType, EN_LANGUAGE);
        preferencesView.addFilterPreference
                (originalFragment, newsType, EN_LANGUAGE, FILTER_PREFERENCE_LANGUAGE_ID);
        enButton.setSelected(true);

        enButton.setEnabled(true);
        ruButton.setEnabled(true);
        frButton.setEnabled(true);
        esButton.setEnabled(true);

        languageButtonsDisabled = false;
    }
}