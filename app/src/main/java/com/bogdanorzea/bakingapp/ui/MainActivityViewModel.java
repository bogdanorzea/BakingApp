package com.bogdanorzea.bakingapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.bogdanorzea.bakingapp.data.ReceiptsRepository;
import com.bogdanorzea.bakingapp.data.database.Receipt;

import java.util.List;

public class MainActivityViewModel extends ViewModel {
    private ReceiptsRepository mRepository;
    private LiveData<List<Receipt>> mReceipts;

    public MainActivityViewModel(ReceiptsRepository repository) {
        this.mRepository = repository;
        this.mReceipts = mRepository.getReceipts();
    }

    public LiveData<List<Receipt>> getReceipts() {
        return mReceipts;
    }
}
