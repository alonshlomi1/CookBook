package com.example.cookbook.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.Models.Comment;
import com.example.cookbook.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private ArrayList<Comment> commentList;
    private Context context;
    private int expandedPosition = -1;

    public CommentAdapter(Context context) {
        this.context = context;
        this.commentList = new ArrayList<>();
    }
    public CommentAdapter(Context context, ArrayList<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Comment comment = commentList.get(position);
        holder.bind(comment);

        final boolean isExpanded = position == expandedPosition;


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
        return commentList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView usernameTextView, commentTextView, emailTextView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            commentTextView = itemView.findViewById(R.id.comment_text);
            emailTextView = itemView.findViewById(R.id.emailTextView);
        }

        public void bind(Comment comment) {
            usernameTextView.setText(comment.getUserName());
            emailTextView.setText(comment.getUserId());
            // Check if the comment is expanded
            if (getAdapterPosition() == expandedPosition) {
                // If expanded, show the entire comment
                commentTextView.setText(comment.getComment());
                emailTextView.setVisibility(View.VISIBLE);
            } else {
                // If not expanded, show only the first 7 characters of the comment
                String shortComment;
                int maxLength = 55; // Maximum length for the shortened comment

                int newlineIndex = comment.getComment().indexOf('\n');
                if (newlineIndex != -1 && newlineIndex <= maxLength) {
                    shortComment = comment.getComment().substring(0, newlineIndex);
                } else {
                    shortComment = comment.getComment().substring(0, Math.min(comment.getComment().length(), maxLength));
                }
                if (comment.getComment().length() > maxLength || newlineIndex != -1) {
                    shortComment += "..";
                }
                commentTextView.setText(shortComment);
                emailTextView.setVisibility(View.GONE);
            }
        }
    }
}
