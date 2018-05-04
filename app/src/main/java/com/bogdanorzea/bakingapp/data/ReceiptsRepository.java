package com.bogdanorzea.bakingapp.data;

import android.arch.lifecycle.LiveData;

import com.bogdanorzea.bakingapp.AppExecutors;
import com.bogdanorzea.bakingapp.data.database.Receipt;
import com.bogdanorzea.bakingapp.data.database.ReceiptDao;
import com.bogdanorzea.bakingapp.data.network.ReceiptsNetworkDataSource;

import java.util.List;

import timber.log.Timber;

public class ReceiptsRepository {
    private static final Object LOCK = new Object();
    private static ReceiptsRepository sInstance;
    private final AppExecutors mExecutors;
    private final ReceiptDao mReceiptsDao;
    private final ReceiptsNetworkDataSource mReceiptsNetworkDataSource;
    private boolean mInitialized;

    private ReceiptsRepository(AppExecutors executors,
                               ReceiptDao receiptDao,
                               ReceiptsNetworkDataSource receiptsNetworkDataSource) {
        this.mReceiptsNetworkDataSource = receiptsNetworkDataSource;
        this.mReceiptsDao = receiptDao;
        this.mExecutors = executors;

        LiveData<List<Receipt>> networkData = receiptsNetworkDataSource.getDownloadedReceipts();
        networkData.observeForever(newReceiptsFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                mReceiptsDao.bulkInsert(newReceiptsFromNetwork);
                Timber.d("New receipts inserted in the database");
            });
        });
    }

    public synchronized static ReceiptsRepository getInstance(AppExecutors executors, ReceiptDao receiptDao, ReceiptsNetworkDataSource receiptsNetworkDataSource) {
        Timber.d("Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new ReceiptsRepository(executors, receiptDao, receiptsNetworkDataSource);
                Timber.d("New repository created");
            }
        }

        return sInstance;
    }

    public LiveData<List<Receipt>> getReceipts() {
        initialize();

        return mReceiptsDao.getAllReceipts();
    }

    public LiveData<Receipt> getReceiptById(int id){
        return mReceiptsDao.getReceiptById(id);
    }

    private void initialize() {
        if (mInitialized) return;
        mInitialized = true;

        startFetchReceiptsService();
    }

    private void startFetchReceiptsService() {
        mReceiptsNetworkDataSource.startFetchReceiptsService();
    }
}
