package com.acrutchfield.natashashairstudio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.acrutchfield.natashashairstudio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_REF = "productRef";

    FirebaseStorage storage = FirebaseStorage.getInstance();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference productInfoRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);


        ImageView ivDetailImage = findViewById(R.id.iv_details_image);
        TextView tvCollectionTitle = findViewById(R.id.tv_details_collection_title);

        Intent intent = getIntent();
        String productRef = intent.getStringExtra(EXTRA_PRODUCT_REF);
        StorageReference productPhotoRef = storage.getReference(productRef + ".png");

        GlideApp.with(this)
                .load(productPhotoRef)
                .into(ivDetailImage);

        DocumentReference testRef = db.document("/PRODUCT_COLLECTIONS/hair/Beautiful Brazilian Body Wave/4x4 Lace Closure");
        // TODO: 2/13/19 prodctInfoRef isn't a valide location in the Firestore DB
        productInfoRef = db.document("/" + productRef);
        testRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                String title = document.get("title").toString();
                tvCollectionTitle.setText(title);
            }
        });

    }
}
