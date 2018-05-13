package com.bogdanorzea.bakingapp.ui.main;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.bogdanorzea.bakingapp.data.RecipesRepository;

public class RecipeListViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final RecipesRepository repository;

    public RecipeListViewModelFactory(RecipesRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new RecipeListViewModel(repository);
    }
}
