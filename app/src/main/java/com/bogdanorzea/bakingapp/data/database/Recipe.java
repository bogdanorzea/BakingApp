package com.bogdanorzea.bakingapp.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "recipes")
public class Recipe {
    @PrimaryKey
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @Ignore
    @SerializedName("ingredients")
    private List<Ingredient> ingredients = null;
    @Ignore
    @SerializedName("steps")
    private List<Step> steps = null;
    @SerializedName("servings")
    private Integer servings;
    @SerializedName("image")
    private String image;

    public Recipe(Integer id, String name, Integer servings, String image) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
