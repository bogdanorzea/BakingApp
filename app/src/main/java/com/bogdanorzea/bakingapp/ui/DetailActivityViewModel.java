package com.bogdanorzea.bakingapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.bogdanorzea.bakingapp.data.ReceiptsRepository;
import com.bogdanorzea.bakingapp.data.database.Receipt;

public class DetailActivityViewModel extends ViewModel {
    private ReceiptsRepository mRepository;
    private LiveData<Receipt> mReceipt;

    public DetailActivityViewModel(ReceiptsRepository repository, int id) {
        this.mRepository = repository;
        this.mReceipt = mRepository.getReceiptById(id);
    }

    public LiveData<Receipt> getReceipt() {
        return mReceipt;
    }
}
