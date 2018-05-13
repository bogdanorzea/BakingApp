package com.bogdanorzea.bakingapp.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.bogdanorzea.bakingapp.data.RecipesRepository;
import com.bogdanorzea.bakingapp.data.database.Recipe;

import java.util.List;

public class RecipeListViewModel extends ViewModel {
    private RecipesRepository repository;
    private LiveData<List<Recipe>> recipes;

    public RecipeListViewModel(RecipesRepository repository) {
        this.repository = repository;
        this.recipes = this.repository.getRecipes();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }
}
