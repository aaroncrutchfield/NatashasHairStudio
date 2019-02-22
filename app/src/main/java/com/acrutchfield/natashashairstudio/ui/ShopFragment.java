package com.acrutchfield.natashashairstudio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.model.ProductCollection;
import com.acrutchfield.natashashairstudio.viewmodel.ShopViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShopFragment extends Fragment implements ProductCollectionAdapter.CollectionInteractionListener {

    private static final String COLLECTION_TITLE = "collection_title";
    private ShopViewModel mViewModel;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference hairMetaRef = db.collection("/HAIR_COLLECTION_META_DATA");
    private ProductCollectionAdapter adapter;

    static ShopFragment newInstance() {
        return new ShopFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        setupRecyclerView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ShopViewModel.class);
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void setupRecyclerView(View view) {
        Query query = hairMetaRef.orderBy("title");

        FirestoreRecyclerOptions<ProductCollection> options = new FirestoreRecyclerOptions.Builder<ProductCollection>()
                .setQuery(query, ProductCollection.class)
                .build();

        adapter = new ProductCollectionAdapter(options, this, getContext());
        RecyclerView recyclerView = view.findViewById(R.id.rv_product_collections);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCollectionInteraction(String collectionTitle) {
        Intent intent = new Intent(getContext(), CollectionDetailsActivity.class);
        intent.putExtra(COLLECTION_TITLE, collectionTitle);
        startActivity(intent);
    }
}
