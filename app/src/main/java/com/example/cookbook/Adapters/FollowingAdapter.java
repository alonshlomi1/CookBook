package com.example.cookbook.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.Models.Comment;
import com.example.cookbook.Models.Following;
import com.example.cookbook.R;
import com.example.cookbook.Utilities.SingleManager;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder> {

    private Following following;
    private ArrayList<String> folowingList_name;
    private ArrayList<String> folowingList_id;
    private Context context;
    private int expandedPosition = -1;

    public FollowingAdapter(Context context) {
            this.context = context;
            this.following = new Following();
            this.folowingList_name = new ArrayList<String>(following.getFollowing().values());
            this.folowingList_id = new ArrayList<String>(following.getFollowing().keySet());
    }
    public FollowingAdapter(Context context, Following following) {
            this.context = context;
            this.following = following;
            this.folowingList_name = new ArrayList<String>(following.getFollowing().values());
            this.folowingList_id = new ArrayList<String>(following.getFollowing().keySet());

    }

    @NonNull
    @Override
    public FollowingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_following_item, parent, false);
            return new FollowingViewHolder(view);
            }

    @Override
    public void onBindViewHolder(@NonNull FollowingViewHolder holder, @SuppressLint("RecyclerView") int position) {
            if(folowingList_id != null && folowingList_name != null){
                holder.bind(folowingList_id.get(position), folowingList_name.get(position));
            }

    final boolean isExpanded = position == expandedPosition;

            //holder.userIdTextView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
            if (isExpanded) {
            expandedPosition = -1; // Collapse the currently expanded item
            } else {
            int prevExpandedPosition = expandedPosition; // Store the previous expanded position
            expandedPosition = position; // Set the newly expanded position
            notifyItemChanged(prevExpandedPosition); // Collapse the previous expanded item
            }
            notifyItemChanged(position); // Expand or collapse the clicked item
            }
            });
            }

    @Override
    public int getItemCount() {
            return folowingList_id.size();
            }

    public class FollowingViewHolder extends RecyclerView.ViewHolder {
        private TextView following_TV_name;
        private MaterialButton following_BTN_unfollow;
        private String following_id, following_name;

        public FollowingViewHolder(@NonNull View itemView) {
            super(itemView);
            following_TV_name = itemView.findViewById(R.id.following_TV_name);
            following_BTN_unfollow = itemView.findViewById(R.id.following_BTN_unfollow);
        }

        public void bind(String following_id, String following_name) {
            following_TV_name.setText(following_name);
            following_BTN_unfollow.setOnClickListener(v -> SingleManager.getInstance().getDBManager().unfollow(following_id));
            this.following_id = following_id;
            this.following_name = following_name;
        }
    }

}
