package com.example.cookbook.Utilities;

import android.content.Context;
import android.os.Vibrator;

import com.google.firebase.database.FirebaseDatabase;

public class SingleManager {
    private static SingleManager instance = null;
    private Context context;
    private FirebaseDatabase db;
    private SingleManager(Context context) {

        this.context = context;
        db = FirebaseDatabase.getInstance();
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

}
