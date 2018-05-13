package com.bogdanorzea.bakingapp;

import android.content.Context;

import com.bogdanorzea.bakingapp.data.RecipesRepository;
import com.bogdanorzea.bakingapp.data.database.BakingDatabase;
import com.bogdanorzea.bakingapp.data.network.RecipesNetworkDataSource;
import com.bogdanorzea.bakingapp.ui.detail.StepDetailViewModelFactory;
import com.bogdanorzea.bakingapp.ui.detail.StepListViewModelFactory;
import com.bogdanorzea.bakingapp.ui.main.RecipeListViewModelFactory;

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

    public static RecipeListViewModelFactory provideMainViewViewModelFactory(Context context) {
        RecipesRepository repository = provideRecipesRepository(context.getApplicationContext());

        return new RecipeListViewModelFactory(repository);
    }

    public static StepListViewModelFactory provideDetailViewModelFactory(Context context, int id) {
        RecipesRepository repository = provideRecipesRepository(context.getApplicationContext());

        return new StepListViewModelFactory(repository, id);
    }

    public static StepDetailViewModelFactory provideStepDetailViewModelFactory(Context context, int recipeId, int stepId) {
        RecipesRepository repository = provideRecipesRepository(context.getApplicationContext());

        return new StepDetailViewModelFactory(repository, recipeId, stepId);
    }
}
