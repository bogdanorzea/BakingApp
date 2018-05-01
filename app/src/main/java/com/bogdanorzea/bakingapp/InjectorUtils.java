package com.bogdanorzea.bakingapp;

import android.content.Context;

import com.bogdanorzea.bakingapp.data.ReceiptsRepository;
import com.bogdanorzea.bakingapp.data.database.BakingDatabase;
import com.bogdanorzea.bakingapp.data.network.ReceiptsNetworkDataSource;
import com.bogdanorzea.bakingapp.ui.MainActivityViewModelFactory;

public class InjectorUtils {
    public static ReceiptsNetworkDataSource provideNetworkDataSource(Context context) {
        AppExecutors executors = AppExecutors.getInstance();

        return ReceiptsNetworkDataSource.getInstance(context.getApplicationContext(), executors);
    }

    public static ReceiptsRepository provideReceiptsRepository(Context context) {
        BakingDatabase db = BakingDatabase.getInstance(context);
        AppExecutors executors = AppExecutors.getInstance();
        ReceiptsNetworkDataSource receiptsNetworkDataSource =
                ReceiptsNetworkDataSource.getInstance(context.getApplicationContext(), executors);

        return ReceiptsRepository.getInstance(executors, db.receiptDao(), receiptsNetworkDataSource);
    }

    public static MainActivityViewModelFactory provideMainViewViewModelFactory(Context context) {
        ReceiptsRepository repository = provideReceiptsRepository(context.getApplicationContext());

        return new MainActivityViewModelFactory(repository);
    }
}
