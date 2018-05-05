package com.bogdanorzea.bakingapp.data.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.bogdanorzea.bakingapp.InjectorUtils;

import timber.log.Timber;

public class RecipesSyncIntentService extends IntentService {

    public RecipesSyncIntentService() {
        super("RecipesSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Timber.d("Intent service started");
        RecipesNetworkDataSource networkDataSource = InjectorUtils.provideNetworkDataSource(this.getApplicationContext());
        networkDataSource.getRecipes();
    }
}
