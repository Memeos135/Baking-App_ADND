package com.example.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<String> data;

    RecyclerAdapter (Context context, ArrayList<String> data){
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }
    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = mInflater.inflate(R.layout.recycler_card, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.recipe_name.setText(data.get(i));

        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInflater.getContext().startActivity(new Intent(mInflater.getContext(), RecipeDetails.class)
                .putExtra("position", String.valueOf(i)));
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipe_name;
        ConstraintLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipe_name = itemView.findViewById(R.id.recipeName);
            container = itemView.findViewById(R.id.containerC);
        }
    }
}
