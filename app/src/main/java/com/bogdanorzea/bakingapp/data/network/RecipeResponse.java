package com.bogdanorzea.bakingapp.data.network;

import com.bogdanorzea.bakingapp.data.database.Ingredient;
import com.bogdanorzea.bakingapp.data.database.Step;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeResponse {
    @SerializedName("id")
    public Integer id;
    @SerializedName("name")
    public String name;
    @SerializedName("ingredients")
    public List<Ingredient> ingredients = null;
    @SerializedName("steps")
    public List<Step> steps = null;
    @SerializedName("servings")
    public Integer servings;
    @SerializedName("image")
    public String image;

    public RecipeResponse(Integer id, String name, List<Ingredient> ingredients, List<Step> steps, Integer servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }
}
