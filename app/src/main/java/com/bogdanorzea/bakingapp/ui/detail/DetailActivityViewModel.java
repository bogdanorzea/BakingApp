package com.bogdanorzea.bakingapp.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.bogdanorzea.bakingapp.data.RecipesRepository;
import com.bogdanorzea.bakingapp.data.database.Recipe;

public class DetailActivityViewModel extends ViewModel {
    private RecipesRepository mRepository;
    private LiveData<Recipe> mRecipe;

    public DetailActivityViewModel(RecipesRepository repository, int id) {
        this.mRepository = repository;
        this.mRecipe = mRepository.getRecipeById(id);
    }

    public LiveData<Recipe> getRecipe() {
        return mRecipe;
    }
}
