package com.example.cookbook.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookbook.Models.Comment;
import com.example.cookbook.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder>{
    private ArrayList<String> instructionsList;
    private Context context;

    public InstructionAdapter(Context context) {
        this.context = context;
        this.instructionsList = new ArrayList<>();
    }
    public InstructionAdapter(Context context, ArrayList<String> instructionsList) {
        this.context = context;
        this.instructionsList = instructionsList;
    }

    @NonNull
    @Override
    public InstructionAdapter.InstructionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.instruction_item, parent, false);
        return new  InstructionAdapter.InstructionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionAdapter.InstructionViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String instruction = instructionsList.get(position);
        holder.bind(instruction);
    }

    @Override
    public int getItemCount() {
        return instructionsList.size();
    }


    public class InstructionViewHolder extends RecyclerView.ViewHolder {
        private MaterialTextView editTextInstruction_itemName;

        public InstructionViewHolder(@NonNull View itemView) {
            super(itemView);
            editTextInstruction_itemName = itemView.findViewById(R.id.editTextInstruction_itemName);
        }
        public void bind(String instruction) {
            editTextInstruction_itemName.setText(instruction);
        }
    }
}
