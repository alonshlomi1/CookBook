package com.example.cookbook.Utilities;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class SingleManager {
    private static SingleManager instance = null;
    private Context context;
    private static FirebaseFirestore db;
    private static Vibrator vibrator;

    private SingleManager(Context context) {
        this.context = context;
    }

    public static SingleManager getInstance() {
        return instance;
    }
    public static void init(Context context) {
        synchronized (SingleManager.class) {
            if (instance == null) {
                instance = new SingleManager(context);
                db = FirebaseFirestore.getInstance();
                vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

            }
        }
    }

    public FirebaseFirestore getDb() {
        return db;
    }
    public void toast(String text) {
        Toast.makeText(
                        context
                        , text
                        , Toast.LENGTH_SHORT)
                .show();    }
    public void vibrate(long milliseconds) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(500);
        }
    }
}
