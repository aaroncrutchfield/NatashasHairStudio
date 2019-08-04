package com.acrutchfield.natashashairstudio.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.model.Product;
import com.acrutchfield.natashashairstudio.shop.ProductDetailsActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class WishListActivity extends AppCompatActivity implements WishListAdapter.WishListInteractionListener {

    private static final String EXTRA_PRODUCT_REF = "productRef";
    private static final String TITLE = "title";
    private final String WISHLIST_COLLECTION = "/WISHLIST/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/default_list/";
    private CollectionReference wishListRef;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private WishListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        Toolbar toolbar = findViewById(R.id.toolbar_wish_list);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        wishListRef = db.collection(WISHLIST_COLLECTION);

        setupRecyclerView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        Query query = wishListRef.orderBy(TITLE);

        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();

        adapter = new WishListAdapter(options, this, this);
        RecyclerView recyclerView = findViewById(R.id.rv_wish_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onWishListInteraction(String productRef) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra(EXTRA_PRODUCT_REF, productRef);
        startActivity(intent);
    }
}
