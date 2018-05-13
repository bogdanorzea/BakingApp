package com.bogdanorzea.bakingapp.ui.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.bogdanorzea.bakingapp.data.RecipesRepository;

public class StepDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final RecipesRepository repository;
    private int stepId;
    private int recipeId;

    public StepDetailViewModelFactory(RecipesRepository repository, int recipeId, int stepId) {
        this.repository = repository;
        this.recipeId = recipeId;
        this.stepId = stepId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new StepDetailViewModel(repository, recipeId, stepId);
    }
}
