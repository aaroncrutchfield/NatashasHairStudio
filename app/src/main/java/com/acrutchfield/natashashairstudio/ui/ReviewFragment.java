package com.acrutchfield.natashashairstudio.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.viewmodel.ReviewViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class ReviewFragment extends Fragment {

    private ReviewViewModel mViewModel;

    public static ReviewFragment newInstance() {
        return new ReviewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ReviewViewModel.class);
        // TODO: Use the ViewModel
    }

}
