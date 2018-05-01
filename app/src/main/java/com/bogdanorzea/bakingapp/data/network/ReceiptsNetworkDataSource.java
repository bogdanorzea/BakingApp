package com.bogdanorzea.bakingapp.data.network;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;

import com.bogdanorzea.bakingapp.AppExecutors;
import com.bogdanorzea.bakingapp.data.database.Receipt;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class ReceiptsNetworkDataSource {
    private static final Object LOCK = new Object();
    private static ReceiptsNetworkDataSource sInstance;
    private final MutableLiveData<List<Receipt>> mDownloadedReceipts;
    private final Context mContext;
    private final AppExecutors mExecutors;

    private ReceiptsNetworkDataSource(Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;
        mDownloadedReceipts = new MutableLiveData<>();
    }

    public synchronized static ReceiptsNetworkDataSource getInstance(Context context, AppExecutors executors) {
        Timber.d("Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new ReceiptsNetworkDataSource(context.getApplicationContext(), executors);
                Timber.d("New network data source created");
            }
        }

        return sInstance;
    }

    public MutableLiveData<List<Receipt>> getDownloadedReceipts() {
        return mDownloadedReceipts;
    }

    public void getReceipts() {
        String API_BASE_URL = "https://d17h27t6h515a5.cloudfront.net";

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        ResponseApiService apiService = retrofit.create(ResponseApiService.class);

        Call<List<Receipt>> call = apiService.getReceipts();
        call.enqueue(new Callback<List<Receipt>>() {
            @Override
            public void onResponse(Call<List<Receipt>> call, Response<List<Receipt>> response) {
                if (response.isSuccessful()) {
                    mDownloadedReceipts.postValue(response.body());
                } else {
                    Timber.e("Response code %s and raw body: %s.", response.code(), response.raw());
                }
            }

            @Override
            public void onFailure(Call<List<Receipt>> call, Throwable t) {
                Timber.d(t.toString());
            }
        });
    }

    public void startFetchReceiptsService() {
        Intent intentToFetch = new Intent(mContext, ReceiptsSyncIntentService.class);
        mContext.startService(intentToFetch);
    }
}
