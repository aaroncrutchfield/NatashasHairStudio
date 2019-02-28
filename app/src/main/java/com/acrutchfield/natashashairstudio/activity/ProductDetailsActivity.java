package com.acrutchfield.natashashairstudio.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.model.Product;
import com.acrutchfield.natashashairstudio.utils.GlideApp;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ProductDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_PRODUCT_REF = "productRef";
    private static final String WISHLIST = "/WISHLIST/";
    private static final String DEFAULT_LIST = "/default_list/";
    private static final String LOG_IN_FIRST = "You must log in first.";
    private static final String ADDED_TO_WISH_LIST = "Added to wish list";
    private static final String TRY_AGAIN = "Error. Try again later";
    private static final String FORMAT_MONEY = "$%s.00";

    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    private FirebaseAuth auth;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ImageView ivDetailImage;
    private TextView tvCollectionTitle;
    private TextView tvProductTitle;
    private TextView tvPriceRange;
    private Spinner spLengthOptions;
    private String wishlistPath;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Toolbar toolbar = findViewById(R.id.toolbar_product);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();

        ivDetailImage = findViewById(R.id.iv_details_image);
        tvCollectionTitle = findViewById(R.id.tv_details_collection_title);
        tvProductTitle = findViewById(R.id.tv_details_title);
        tvPriceRange = findViewById(R.id.tv_price);
        spLengthOptions = findViewById(R.id.sp_length_options);
        Button btnWishList = findViewById(R.id.btn_wish_list);


        Intent intent = getIntent();
        String productRef = intent.getStringExtra(EXTRA_PRODUCT_REF);

        DocumentReference productInfoRef = db.document(productRef);
        productInfoRef.get().addOnCompleteListener(this::updateUI);

        btnWishList.setOnClickListener(v -> {
            if (auth.getCurrentUser() != null) {
                wishlistPath = WISHLIST + auth.getCurrentUser().getUid() + DEFAULT_LIST;
                addItemToWishList();
            } else {
                notifyUser(LOG_IN_FIRST);
            }
        });
    }

    private void addItemToWishList() {
        String wishlistRef = wishlistPath + getWishlistTitle();
        DocumentReference wishListdoc = db.document(wishlistRef);

        wishListdoc.set(product)
                .addOnCompleteListener(task -> notifyUser(ADDED_TO_WISH_LIST))
                .addOnFailureListener(e -> notifyUser(TRY_AGAIN));
    }

    private void updateUI(Task<DocumentSnapshot> task) {
        Map<String, Integer> map = new HashMap<>();
        DocumentSnapshot doc = task.getResult();

        assert doc != null;
        product = doc.toObject(Product.class);

        assert product != null;
        tvProductTitle.setText(product.getTitle());
        tvCollectionTitle.setText(product.getCollection());

        StorageReference productPhotoRef = storage.getReference(product.getImageUrl());

        GlideApp.with(this)
                .load(productPhotoRef)
                .into(ivDetailImage);


        Map<String, Integer> length = product.getLength();
        length.forEach(map::put);

        List<String> sizes = new ArrayList<>(length.keySet());
        Collections.sort(sizes);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner, sizes);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spLengthOptions.setAdapter(adapter);

        spLengthOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                String key = tv.getText().toString();
                Integer price = map.get(key);

                String priceString = String.format(FORMAT_MONEY,price);
                tvPriceRange.setText(priceString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    private String getWishlistTitle() {
        return product.getCollection() + "." + product.getTitle();
    }

    private void notifyUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
