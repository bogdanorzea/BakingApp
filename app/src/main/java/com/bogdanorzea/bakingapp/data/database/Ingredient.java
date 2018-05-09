package com.bogdanorzea.bakingapp.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "ingredients",
        primaryKeys = {"recipeId", "name"},
        foreignKeys = {@ForeignKey(entity = RecipeInfo.class,
                parentColumns = "id",
                childColumns = "recipeId",
                onDelete = CASCADE)})
public class Ingredient {
    @NonNull
    private Integer recipeId;
    @SerializedName("quantity")
    private Double quantity;
    @SerializedName("measure")
    private String measure;
    @NonNull
    @SerializedName("ingredient")
    private String name;

    public Ingredient(@NonNull Integer recipeId, Double quantity, @NonNull String measure, String name) {
        this.recipeId = recipeId;
        this.quantity = quantity;
        this.measure = measure;
        this.name = name;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(@NonNull Integer recipeId) {
        this.recipeId = recipeId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }
}
