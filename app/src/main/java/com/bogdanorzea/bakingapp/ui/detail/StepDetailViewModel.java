package com.bogdanorzea.bakingapp.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.bogdanorzea.bakingapp.data.RecipesRepository;
import com.bogdanorzea.bakingapp.data.database.Step;

public class StepDetailViewModel extends ViewModel {
    private RecipesRepository repository;
    private LiveData<Step> step;

    public StepDetailViewModel(RecipesRepository repository, int recipeId, int stepId) {
        this.repository = repository;
        this.step = this.repository.getStepByRecipeIdStepId(recipeId, stepId);
    }

    public LiveData<Step> getStep() {
        return step;
    }
}
