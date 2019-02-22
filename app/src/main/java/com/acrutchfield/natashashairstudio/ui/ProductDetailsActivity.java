package com.acrutchfield.natashashairstudio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.acrutchfield.natashashairstudio.R;
import com.acrutchfield.natashashairstudio.model.Product;
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

    public static final String EXTRA_PRODUCT_REF = "productRef";

    FirebaseStorage storage = FirebaseStorage.getInstance();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference productInfoRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Toolbar toolbar = findViewById(R.id.toolbar_product);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);


        ImageView ivDetailImage = findViewById(R.id.iv_details_image);
        TextView tvCollectionTitle = findViewById(R.id.tv_details_collection_title);
        TextView tvProductTitle = findViewById(R.id.tv_details_title);
        TextView tvPriceRange = findViewById(R.id.tv_price);
        Spinner spLengthOptions = findViewById(R.id.sp_length_options);

        Intent intent = getIntent();
        String productRef = intent.getStringExtra(EXTRA_PRODUCT_REF);
        StorageReference productPhotoRef = storage.getReference(productRef + ".png");

        GlideApp.with(this)
                .load(productPhotoRef)
                .into(ivDetailImage);


        String infoPath = "/" + productRef;
        productInfoRef = db.document(infoPath);

        Map<String, Integer> map = new HashMap<>();
        productInfoRef.get().addOnCompleteListener(task -> {
            DocumentSnapshot doc = task.getResult();

            assert doc != null;
            Product product = doc.toObject(Product.class);

            assert product != null;
            tvProductTitle.setText(product.getTitle());
            tvCollectionTitle.setText(getCollection(infoPath));

            Map<String, Integer> length = product.getLength();
            Log.d("ProductDetailsActivity", "onCreate.length: " + length.toString());
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

                    String priceString = String.format("$%s.00",price);
                    tvPriceRange.setText(priceString);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
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

    private String getCollection(String infoPath) {
        String[] paths = infoPath.split("/");
        return paths[3];
    }
}
