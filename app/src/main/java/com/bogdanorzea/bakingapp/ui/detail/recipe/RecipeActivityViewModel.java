package com.bogdanorzea.bakingapp.ui.detail.recipe;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.bogdanorzea.bakingapp.data.RecipesRepository;
import com.bogdanorzea.bakingapp.data.database.Ingredient;
import com.bogdanorzea.bakingapp.data.database.Recipe;

import java.util.List;

public class RecipeActivityViewModel extends ViewModel {
    private RecipesRepository mRepository;
    private LiveData<Recipe> mRecipe;
    private LiveData<List<Ingredient>> mIngredients;

    public RecipeActivityViewModel(RecipesRepository repository, int id) {
        this.mRepository = repository;
        this.mRecipe = mRepository.getRecipeById(id);
        this.mIngredients = mRepository.getIngredientsByRecipeId(id);
    }

    public LiveData<List<Ingredient>> getIngredients() {
        return mIngredients;
    }

    public LiveData<Recipe> getRecipe() {
        return mRecipe;
    }
}
