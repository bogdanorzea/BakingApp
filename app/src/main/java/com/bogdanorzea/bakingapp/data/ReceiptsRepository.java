package com.bogdanorzea.bakingapp.data;

import com.bogdanorzea.bakingapp.data.database.Receipt;
import com.bogdanorzea.bakingapp.data.network.ResponseApiService;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class ReceiptsRepository {

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
                    for (Receipt receipt: response.body()) {
                        Timber.d(receipt.getName());
                    }
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
}
