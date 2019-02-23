package com.acrutchfield.natashashairstudio.viewmodel;

import com.acrutchfield.natashashairstudio.data.FirestoreQueryLivedata;
import com.acrutchfield.natashashairstudio.model.Product;
import com.acrutchfield.natashashairstudio.model.ProductCollection;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class ShopViewModel extends ViewModel {
    private static final String ORDER_BY_TITLE = "title";
    private static final String PRODUCTS_BASE_REF = "/PRODUCT_COLLECTIONS/hair/";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionRef = db.collection("/HAIR_COLLECTION_META_DATA");

    private final FirestoreQueryLivedata collectionLiveQuery = new FirestoreQueryLivedata(collectionRef, ORDER_BY_TITLE);

    private final LiveData<List<ProductCollection>> collectionLiveData =
            Transformations.map(collectionLiveQuery, input -> input.toObjects(ProductCollection.class));

    public LiveData<List<Product>> getProductsLiveData(String collectionName) {
        String productsCompleteRef = PRODUCTS_BASE_REF + collectionName;
        CollectionReference productRef = db.collection(productsCompleteRef);
        FirestoreQueryLivedata productLiveQuery = new FirestoreQueryLivedata(productRef, ORDER_BY_TITLE);
        return Transformations.map(productLiveQuery, input -> input.toObjects(Product.class));
    }

    public LiveData<List<ProductCollection>> getCollectionLiveData() {
        return collectionLiveData;
    }

}
