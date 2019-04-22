package com.example.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class IngredientsRecyclerAdapter extends RecyclerView.Adapter<IngredientsRecyclerAdapter.ViewHolder> {

    private ArrayList<IngredientSetter> data;
    private LayoutInflater mInflater;

    IngredientsRecyclerAdapter(Context context, ArrayList<IngredientSetter> data){
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;

        view = mInflater.inflate(R.layout.ingredients_card, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.ingredient_name.setText(data.get(i).getIngredient_name());
        viewHolder.getIngredient_measure.setText(data.get(i).getIngredient_measure());
        viewHolder.ingredient_quantity.setText(data.get(i).getIngredient_quantity());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ingredient_name;
        TextView ingredient_quantity;
        TextView getIngredient_measure;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ingredient_name = itemView.findViewById(R.id.ingredient_name);
            ingredient_quantity = itemView.findViewById(R.id.ingredient_quantity);
            getIngredient_measure = itemView.findViewById(R.id.ingredient_measure);
        }
    }
}
