package com.youthfimodd.elenges.firebase;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class DatabaseFireStore {

    static FirebaseFirestore db;
    private static DocumentSnapshot documentSnapshot = null;
    static final String TAG = "Database";

    public static FirebaseFirestore get() {
        if (db == null) {

            db = FirebaseFirestore.getInstance();

            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build();

            db.setFirestoreSettings(settings);

        }
        return db;
    }
}

