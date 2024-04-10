package com.example.cookbook;

import android.app.Application;
import android.util.Log;

import com.example.cookbook.Utilities.SingleManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SingleManager.init(this);
//        SharedPreferencesManager.init(this);
//        ImageLoader.initImageLoader(this);
    }
}
