package com.acrutchfield.natashashairstudio.data;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

import androidx.lifecycle.LiveData;

public class FirestoreQueryLivedata extends LiveData<QuerySnapshot> {
    private final Query query;
    private final QueryEventListener listener = new QueryEventListener();

    // Query referencing the location of a Collection containing the desired documents.
    FirestoreQueryLivedata(Query query) {
        this.query = query;
    }

    // CollectionReference for the location of the desired documents.
    public FirestoreQueryLivedata(CollectionReference collectionRef, String orderBy) {
        this.query = collectionRef.orderBy(orderBy);
    }

    @Override
    protected void onActive() {
        query.addSnapshotListener(listener);
    }

    @Override
    protected void onInactive() {
        // SnapshotListener automatically removed onStop()
    }


    private class QueryEventListener implements EventListener<QuerySnapshot> {

        @Override
        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
            setValue(queryDocumentSnapshots);

            if (e != null) {
                Log.e("QueryEventListener", "onEvent: ", e);
            }
        }
    }
}
