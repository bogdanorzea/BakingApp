package com.bogdanorzea.bakingapp.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.bogdanorzea.bakingapp.data.RecipesRepository;
import com.bogdanorzea.bakingapp.data.database.Recipe;

public class RecipeViewModel extends ViewModel {
    private RecipesRepository repository;
    private LiveData<Recipe> recipe;

    public RecipeViewModel(RecipesRepository repository, int recipeId) {
        this.repository = repository;
        this.recipe = this.repository.getRecipeById(recipeId);
    }

    public LiveData<Recipe> getRecipe() {
        return recipe;
    }
}
