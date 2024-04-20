package com.example.cookbook.Interfaces;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textview.MaterialTextView;

public interface RefreshHomeListener {
    void refresh(SwipeRefreshLayout home_SWIPE_refresh, int current);

}
