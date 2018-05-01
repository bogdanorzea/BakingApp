package com.bogdanorzea.bakingapp.data.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.bogdanorzea.bakingapp.InjectorUtils;

import timber.log.Timber;

public class ReceiptsSyncIntentService extends IntentService {

    public ReceiptsSyncIntentService() {
        super("ReceiptsSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Timber.d("Intent service started");
        ReceiptsNetworkDataSource networkDataSource = InjectorUtils.provideNetworkDataSource(this.getApplicationContext());
        networkDataSource.getReceipts();
    }
}
