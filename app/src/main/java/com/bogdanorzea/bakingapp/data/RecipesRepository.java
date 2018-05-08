package com.bogdanorzea.bakingapp.data;

import android.arch.lifecycle.LiveData;

import com.bogdanorzea.bakingapp.AppExecutors;
import com.bogdanorzea.bakingapp.data.database.Ingredient;
import com.bogdanorzea.bakingapp.data.database.IngredientDao;
import com.bogdanorzea.bakingapp.data.database.Recipe;
import com.bogdanorzea.bakingapp.data.database.RecipeDao;
import com.bogdanorzea.bakingapp.data.network.RecipesNetworkDataSource;

import java.util.List;

import timber.log.Timber;

public class RecipesRepository {
    private static final Object LOCK = new Object();
    private static RecipesRepository sInstance;
    private final AppExecutors mExecutors;
    private final RecipeDao mRecipesDao;
    private final IngredientDao mIngredientsDao;
    private final RecipesNetworkDataSource mRecipesNetworkDataSource;
    private boolean mInitialized;

    private RecipesRepository(AppExecutors executors,
                              RecipeDao recipeDao,
                              IngredientDao ingredientDao,
                              RecipesNetworkDataSource recipesNetworkDataSource) {
        this.mRecipesNetworkDataSource = recipesNetworkDataSource;
        this.mIngredientsDao = ingredientDao;
        this.mRecipesDao = recipeDao;
        this.mExecutors = executors;

        LiveData<List<Recipe>> networkData = recipesNetworkDataSource.getDownloadedRecipes();
        networkData.observeForever(newRecipesFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                mRecipesDao.bulkInsert(newRecipesFromNetwork);

                for (Recipe newRecipe : newRecipesFromNetwork) {
                    int newRecipeId = newRecipe.getId();
                    List<Ingredient> ingredients = newRecipe.getIngredients();
                    for (Ingredient ingredient : ingredients) {
                        ingredient.setRecipeId(newRecipeId);
                    }

                    mIngredientsDao.bulkInsert(ingredients);
                }
                // TODO insert steps from recipes to DB
                Timber.d("New recipes inserted in the database");
            });
        });
    }

    public synchronized static RecipesRepository getInstance(AppExecutors executors, RecipeDao recipeDao, IngredientDao ingredientDao, RecipesNetworkDataSource recipesNetworkDataSource) {
        Timber.d("Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new RecipesRepository(executors, recipeDao, ingredientDao, recipesNetworkDataSource);
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

    private void initialize() {
        if (mInitialized) return;
        mInitialized = true;

        startFetchRecipesService();
    }

    private void startFetchRecipesService() {
        mRecipesNetworkDataSource.startFetchRecipesService();
    }
}
