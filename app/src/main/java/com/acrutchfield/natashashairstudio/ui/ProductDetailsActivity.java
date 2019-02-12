package com.acrutchfield.natashashairstudio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.acrutchfield.natashashairstudio.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_FILE_REF = "fileRef";

    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        ImageView ivDetailImage = findViewById(R.id.iv_details_image);

        Intent intent = getIntent();
        String fileRef = intent.getStringExtra(EXTRA_FILE_REF);
        StorageReference storageRef = storage.getReference(fileRef);

        GlideApp.with(this)
                .load(storageRef)
                .into(ivDetailImage);
    }
}
