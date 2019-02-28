package com.acrutchfield.natashashairstudio.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.acrutchfield.natashashairstudio.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class BookAppointmentFragment extends Fragment {

    private static final String URL_STRING = "url_string";

    private String urlString;


    public BookAppointmentFragment() {
        // Required empty public constructor
    }

    public static BookAppointmentFragment newInstance(String urlString) {
        BookAppointmentFragment fragment = new BookAppointmentFragment();
        Bundle args = new Bundle();
        args.putString(URL_STRING, urlString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            urlString = getArguments().getString(URL_STRING);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_book_appointment, container, false);
        WebView webView = view.findViewById(R.id.wv_book_appointment);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(urlString);
        return view;
    }

}
