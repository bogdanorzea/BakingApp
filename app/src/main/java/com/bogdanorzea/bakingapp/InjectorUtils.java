package com.bogdanorzea.bakingapp;

import android.content.Context;

import com.bogdanorzea.bakingapp.data.RecipesRepository;
import com.bogdanorzea.bakingapp.data.database.BakingDatabase;
import com.bogdanorzea.bakingapp.data.network.RecipesNetworkDataSource;
import com.bogdanorzea.bakingapp.ui.detail.RecipeActivityViewModelFactory;
import com.bogdanorzea.bakingapp.ui.main.MainActivityViewModelFactory;

public class InjectorUtils {
    public static RecipesNetworkDataSource provideNetworkDataSource(Context context) {
        AppExecutors executors = AppExecutors.getInstance();

        return RecipesNetworkDataSource.getInstance(context.getApplicationContext(), executors);
    }

    public static RecipesRepository provideRecipesRepository(Context context) {
        BakingDatabase db = BakingDatabase.getInstance(context);
        AppExecutors executors = AppExecutors.getInstance();
        RecipesNetworkDataSource recipesNetworkDataSource =
                RecipesNetworkDataSource.getInstance(context.getApplicationContext(), executors);

        return RecipesRepository.getInstance(executors, db.getRecipeDao(), db.getIngredientDao(), db.getStepDao(), recipesNetworkDataSource);
    }

    public static MainActivityViewModelFactory provideMainViewViewModelFactory(Context context) {
        RecipesRepository repository = provideRecipesRepository(context.getApplicationContext());

        return new MainActivityViewModelFactory(repository);
    }

    public static RecipeActivityViewModelFactory provideDetailViewModelFactory(Context context, int id) {
        RecipesRepository repository = provideRecipesRepository(context.getApplicationContext());

        return new RecipeActivityViewModelFactory(repository, id);
    }
}
