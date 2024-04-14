package com.example.cookbook.Utilities;

import android.content.Context;
import android.os.Vibrator;

import com.google.firebase.firestore.FirebaseFirestore;

public class SingleManager {
    private static SingleManager instance = null;
    private Context context;
    private FirebaseFirestore db;
    private SingleManager(Context context) {

        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    public static SingleManager getInstance() {
        return instance;
    }
    public static void init(Context context) {
        synchronized (SingleManager.class) {
            if (instance == null) {
                instance = new SingleManager(context);
            }
        }
    }

    public FirebaseFirestore getDb() {
        return db;
    }
}
