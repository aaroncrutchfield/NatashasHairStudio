package com.acrutchfield.natashashairstudio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.viewmodel.ShopViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShopFragment extends Fragment implements ProductCollectionAdapter.CollectionInteractionListener {

    private static final String COLLECTION_TITLE = "collection_title";
    private ShopViewModel mViewModel;

    private ProductCollectionAdapter adapter;

    static ShopFragment newInstance() {
        return new ShopFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        mViewModel = ViewModelProviders.of(this).get(ShopViewModel.class);
        setupRecyclerView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void setupRecyclerView(View view) {
        adapter = new ProductCollectionAdapter(this, getContext());
        RecyclerView recyclerView = view.findViewById(R.id.rv_product_collections);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);

        mViewModel.getCollectionLiveData().observe(this,
                productCollections -> adapter.updateProductCollections(productCollections));
    }

    @Override
    public void onCollectionInteraction(String collectionTitle) {
        Intent intent = new Intent(getContext(), CollectionDetailsActivity.class);
        intent.putExtra(COLLECTION_TITLE, collectionTitle);
        startActivity(intent);
    }
}
