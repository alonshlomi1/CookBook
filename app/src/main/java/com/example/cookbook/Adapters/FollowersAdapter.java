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

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder> {

    private Following following;
    private ArrayList<String> folowingList_name;
    private ArrayList<String> folowingList_id;
    private Context context;
    private int expandedPosition = -1;

    public FollowersAdapter(Context context) {
        this.context = context;
        this.following = new Following();
        this.folowingList_name = new ArrayList<String>(following.getFollowers().values());
        this.folowingList_id = new ArrayList<String>(following.getFollowers().keySet());
    }
    public FollowersAdapter(Context context, Following following) {
        this.context = context;
        this.following = following;
        this.folowingList_name = new ArrayList<String>(following.getFollowers().values());
        this.folowingList_id = new ArrayList<String>(following.getFollowers().keySet());

    }

    @NonNull
    @Override
    public FollowersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_follower_item, parent, false);
        return new FollowersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowersViewHolder holder, @SuppressLint("RecyclerView") int position) {
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

    public class FollowersViewHolder extends RecyclerView.ViewHolder {
        private TextView following_TV_name;
        private String following_id, following_name;

        public FollowersViewHolder(@NonNull View itemView) {
            super(itemView);
            following_TV_name = itemView.findViewById(R.id.following_TV_name);
        }

        public void bind(String following_id, String following_name) {
            following_TV_name.setText(following_name);
            this.following_id = following_id;
            this.following_name = following_name;
        }
    }

}

