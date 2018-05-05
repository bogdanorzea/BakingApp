package com.bogdanorzea.bakingapp.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.bogdanorzea.bakingapp.data.RecipesRepository;
import com.bogdanorzea.bakingapp.data.database.Recipe;

import java.util.List;

public class MainActivityViewModel extends ViewModel {
    private RecipesRepository mRepository;
    private LiveData<List<Recipe>> mRecipes;

    public MainActivityViewModel(RecipesRepository repository) {
        this.mRepository = repository;
        this.mRecipes = mRepository.getRecipes();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }
}
