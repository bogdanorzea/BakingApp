package com.bogdanorzea.bakingapp.data;

import android.arch.lifecycle.LiveData;

import com.bogdanorzea.bakingapp.AppExecutors;
import com.bogdanorzea.bakingapp.data.database.Ingredient;
import com.bogdanorzea.bakingapp.data.database.IngredientDao;
import com.bogdanorzea.bakingapp.data.database.Recipe;
import com.bogdanorzea.bakingapp.data.database.RecipeDao;
import com.bogdanorzea.bakingapp.data.database.RecipeInfo;
import com.bogdanorzea.bakingapp.data.database.Step;
import com.bogdanorzea.bakingapp.data.database.StepDao;
import com.bogdanorzea.bakingapp.data.network.RecipeResponse;
import com.bogdanorzea.bakingapp.data.network.RecipesNetworkDataSource;

import java.util.List;

import timber.log.Timber;

public class RecipesRepository {
    private static final Object LOCK = new Object();
    private static RecipesRepository sInstance;
    private final AppExecutors mExecutors;
    private final RecipeDao mRecipesDao;
    private final IngredientDao mIngredientsDao;
    private final StepDao mStepsDao;
    private final RecipesNetworkDataSource mRecipesNetworkDataSource;
    private boolean mInitialized;

    private RecipesRepository(AppExecutors executors,
                              RecipeDao recipeDao,
                              IngredientDao ingredientDao,
                              StepDao stepDao,
                              RecipesNetworkDataSource recipesNetworkDataSource) {
        this.mStepsDao = stepDao;
        this.mRecipesNetworkDataSource = recipesNetworkDataSource;
        this.mIngredientsDao = ingredientDao;
        this.mRecipesDao = recipeDao;
        this.mExecutors = executors;

        LiveData<List<RecipeResponse>> networkData = recipesNetworkDataSource.getDownloadedRecipes();
        networkData.observeForever(newRecipesFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                if (newRecipesFromNetwork != null) {
                    for (RecipeResponse newRecipe : newRecipesFromNetwork) {
                        int newRecipeId = newRecipe.id;

                        // Insert recipe in database
                        RecipeInfo recipeInfo = new RecipeInfo(newRecipeId, newRecipe.name, newRecipe.servings, newRecipe.image);
                        mRecipesDao.insert(recipeInfo);

                        // Insert ingredients in database
                        List<Ingredient> ingredients = newRecipe.ingredients;
                        for (Ingredient ingredient : ingredients) {
                            ingredient.setRecipeId(newRecipeId);
                        }
                        mIngredientsDao.bulkInsert(ingredients);

                        // Insert steps in database
                        List<Step> steps = newRecipe.steps;
                        for (Step step : steps) {
                            step.setRecipeId(newRecipeId);
                        }
                        mStepsDao.bulkInsert(steps);
                    }

                    Timber.i("New recipes inserted in the database");
                } else {
                    Timber.e("Error downloading the recipes from the server");
                }
            });
        });
    }

    public synchronized static RecipesRepository getInstance(AppExecutors executors, RecipeDao recipeDao, IngredientDao ingredientDao, StepDao stepDao, RecipesNetworkDataSource recipesNetworkDataSource) {
        Timber.d("Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new RecipesRepository(executors, recipeDao, ingredientDao, stepDao, recipesNetworkDataSource);
                Timber.d("New repository created");
            }
        }

        return sInstance;
    }

    public LiveData<List<Recipe>> getRecipes() {
        initialize();

        return mRecipesDao.getRecipes();
    }

    public LiveData<Recipe> getRecipeById(int id) {
        return mRecipesDao.getRecipeById(id);
    }

    public LiveData<List<Ingredient>> getIngredientsByRecipeId(int recipeId) {
        return mIngredientsDao.getIngredientsByRecipeId(recipeId);
    }

    public LiveData<List<Step>> getStepsByRecipeId(int recipeId) {
        return mStepsDao.getStepsByRecipeId(recipeId);
    }

    public LiveData<Step> getStepByRecipeIdStepId(int recipeId, int stepId) {
        return mStepsDao.getStepsByRecipeIdStepId(recipeId, stepId);
    }

    private void initialize() {
        if (mInitialized) return;
        mInitialized = true;

        startFetchRecipesService();
    }

    private void startFetchRecipesService() {
        mRecipesNetworkDataSource.startFetchRecipesService();
    }
}
