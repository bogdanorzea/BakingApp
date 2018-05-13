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

    public String getStringIngredients() {
        StringBuffer buffer = new StringBuffer("");

        if (ingredients != null) {
            for (Ingredient ingredient : ingredients) {
                buffer.append(ingredient.getName()).append('\n');
            }
        }

        return buffer.toString().trim();
    }
}
