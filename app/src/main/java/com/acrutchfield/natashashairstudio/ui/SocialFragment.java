package com.acrutchfield.natashashairstudio.ui;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.acrutchfield.natashashairstudio.R;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SocialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SocialFragment extends Fragment {



    public SocialFragment() {
        // Required empty public constructor
    }


    static SocialFragment newInstance() {
        return new SocialFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_social, container, false);
        ImageView ivInstagram = view.findViewById(R.id.iv_instagram);
        ImageView ivFacebook = view.findViewById(R.id.iv_facebook);

        ivInstagram.setOnClickListener(v -> launchInstagram());
        ivFacebook.setOnClickListener(v -> launchFacebook());

        return view;
    }

    // https://stackoverflow.com/questions/21505941/intent-to-open-instagram-user-profile-on-android
    private void launchInstagram() {
        String instagramPageID = "natashashairstudio";
        Uri appUri = Uri.parse("http://instagram.com/_u/" + instagramPageID);
        Uri webUri = Uri.parse("http://instagram.com/" + instagramPageID);

        try {
            startActivity(new Intent(Intent.ACTION_VIEW, appUri)
                        .setPackage("com.instagram.android"));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, webUri));
        }
    }

    // https://gist.github.com/takeshiyako2/8b26bcdd8f2ad4da4fca
    private void launchFacebook() {
        String facebookPageID = "natashashairstudio1";

        String facebookUrl = "https://www.facebook.com/" + facebookPageID;

        String facebookUrlScheme = "fb://page/" + facebookPageID;

        try {
            int versionCode = Objects.requireNonNull(getContext())
                    .getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0)
                    .versionCode;

            if (versionCode >= 3002850) {
                Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } else {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrlScheme)));
            }
        } catch (PackageManager.NameNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
        }
    }

}
