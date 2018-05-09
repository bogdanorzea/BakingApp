package com.bogdanorzea.bakingapp.data.database;

import android.arch.persistence.room.Relation;

import java.util.List;

public class Recipe extends RecipeInfo {

    @Relation(parentColumn = "id", entityColumn = "recipeId")
    public List<Ingredient> ingredients;

    @Relation(parentColumn = "id", entityColumn = "recipeId")
    public List<Step> steps;

    public Recipe(Integer id, String name, Integer servings, String image) {
        super(id, name, servings, image);
    }
}
