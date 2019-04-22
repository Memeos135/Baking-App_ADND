package com.example.bakingapp;

import android.os.Parcel;
import android.os.Parcelable;

public class IngredientSetter implements Parcelable {
    private String ingredient_name;
    private String ingredient_quantity;
    private String ingredient_measure;

    protected IngredientSetter(Parcel in) {
        ingredient_name = in.readString();
        ingredient_quantity = in.readString();
        ingredient_measure = in.readString();
    }

    public static final Creator<IngredientSetter> CREATOR = new Creator<IngredientSetter>() {
        @Override
        public IngredientSetter createFromParcel(Parcel in) {
            return new IngredientSetter(in);
        }

        @Override
        public IngredientSetter[] newArray(int size) {
            return new IngredientSetter[size];
        }
    };

    public String getIngredient_name() {
        return ingredient_name;
    }

    public void setIngredient_name(String ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    public String getIngredient_quantity() {
        return ingredient_quantity;
    }

    public void setIngredient_quantity(String ingredient_quantity) {
        this.ingredient_quantity = ingredient_quantity;
    }

    public String getIngredient_measure() {
        return ingredient_measure;
    }

    public void setIngredient_measure(String ingredient_measure) {
        this.ingredient_measure = ingredient_measure;
    }

    public IngredientSetter() {
    }

    public IngredientSetter(String ingredient_name, String ingredient_quantity, String ingredient_measure) {
        this.ingredient_name = ingredient_name;
        this.ingredient_quantity = ingredient_quantity;
        this.ingredient_measure = ingredient_measure;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ingredient_name);
        parcel.writeString(ingredient_quantity);
        parcel.writeString(ingredient_measure);
    }
}
