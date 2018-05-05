package com.bogdanorzea.bakingapp.data;

import android.arch.lifecycle.LiveData;

import com.bogdanorzea.bakingapp.AppExecutors;
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
    private final RecipesNetworkDataSource mRecipesNetworkDataSource;
    private boolean mInitialized;

    private RecipesRepository(AppExecutors executors,
                              RecipeDao recipeDao,
                              RecipesNetworkDataSource recipesNetworkDataSource) {
        this.mRecipesNetworkDataSource = recipesNetworkDataSource;
        this.mRecipesDao = recipeDao;
        this.mExecutors = executors;

        LiveData<List<Recipe>> networkData = recipesNetworkDataSource.getDownloadedRecipes();
        networkData.observeForever(newRecipesFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                mRecipesDao.bulkInsert(newRecipesFromNetwork);
                Timber.d("New recipes inserted in the database");
            });
        });
    }

    public synchronized static RecipesRepository getInstance(AppExecutors executors, RecipeDao recipeDao, RecipesNetworkDataSource recipesNetworkDataSource) {
        Timber.d("Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new RecipesRepository(executors, recipeDao, recipesNetworkDataSource);
                Timber.d("New repository created");
            }
        }

        return sInstance;
    }

    public LiveData<List<Recipe>> getRecipes() {
        initialize();

        return mRecipesDao.getRecipes();
    }

    public LiveData<Recipe> getRecipeById(int id){
        return mRecipesDao.getRecipeById(id);
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
