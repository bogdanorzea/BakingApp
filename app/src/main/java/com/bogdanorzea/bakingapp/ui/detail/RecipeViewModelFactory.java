package com.bogdanorzea.bakingapp.ui.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.bogdanorzea.bakingapp.data.RecipesRepository;

public class RecipeViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final RecipesRepository repository;
    private int recipeId;

    public RecipeViewModelFactory(RecipesRepository repository, int id) {
        this.repository = repository;
        this.recipeId = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new RecipeViewModel(repository, recipeId);
    }
}
