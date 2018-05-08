package com.bogdanorzea.bakingapp.ui.detail.recipe;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.bogdanorzea.bakingapp.data.RecipesRepository;

public class RecipeActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final RecipesRepository mRepository;
    private int mId;

    public RecipeActivityViewModelFactory(RecipesRepository repository, int id) {
        this.mRepository = repository;
        this.mId = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new RecipeActivityViewModel(mRepository, mId);
    }
}
