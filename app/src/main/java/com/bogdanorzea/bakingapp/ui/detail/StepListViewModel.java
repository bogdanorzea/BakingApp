package com.bogdanorzea.bakingapp.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.bogdanorzea.bakingapp.data.RecipesRepository;
import com.bogdanorzea.bakingapp.data.database.Step;

import java.util.List;

public class StepListViewModel extends ViewModel {
    private RecipesRepository repository;
    private LiveData<List<Step>> steps;

    public StepListViewModel(RecipesRepository repository, int recipeId) {
        this.repository = repository;
        this.steps = this.repository.getStepsByRecipeId(recipeId);
    }

    public LiveData<List<Step>> getSteps() {
        return steps;
    }
}
