package com.bogdanorzea.bakingapp.data.network;

import com.bogdanorzea.bakingapp.data.database.Receipt;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Rest API access point
 */
public interface ResponseApiService {
    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Receipt>> getReceipts();
}
