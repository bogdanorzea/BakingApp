package com.bogdanorzea.bakingapp.data.network;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;

import com.bogdanorzea.bakingapp.AppExecutors;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class RecipesNetworkDataSource {
    private static final Object LOCK = new Object();
    private static RecipesNetworkDataSource sInstance;
    private final MutableLiveData<List<RecipeResponse>> mDownloadedRecipes;
    private final Context mContext;
    private final AppExecutors mExecutors;

    private RecipesNetworkDataSource(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;
        mDownloadedRecipes = new MutableLiveData<>();
    }

    public synchronized static RecipesNetworkDataSource getInstance(Context context, AppExecutors executors) {
        Timber.d("Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new RecipesNetworkDataSource(context.getApplicationContext(), executors);
                Timber.d("New network data source created");
            }
        }

        return sInstance;
    }

    public MutableLiveData<List<RecipeResponse>> getDownloadedRecipes() {
        return mDownloadedRecipes;
    }

    public void getRecipes() {
        String API_BASE_URL = "https://d17h27t6h515a5.cloudfront.net";

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        ResponseApiService apiService = retrofit.create(ResponseApiService.class);

        Call<List<RecipeResponse>> call = apiService.getRecipes();
        call.enqueue(new Callback<List<RecipeResponse>>() {
            @Override
            public void onResponse(Call<List<RecipeResponse>> call, Response<List<RecipeResponse>> response) {
                if (response.isSuccessful()) {
                    mDownloadedRecipes.postValue(response.body());
                } else {
                    Timber.e("Response code %s and raw body: %s.", response.code(), response.raw());
                }
            }

            @Override
            public void onFailure(Call<List<RecipeResponse>> call, Throwable t) {
                Timber.d(t.toString());
            }
        });
    }

    public void startFetchRecipesService() {
        Intent intentToFetch = new Intent(mContext, RecipesSyncIntentService.class);
        mContext.startService(intentToFetch);
    }
}
