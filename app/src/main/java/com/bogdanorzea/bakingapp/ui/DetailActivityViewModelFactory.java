package com.bogdanorzea.bakingapp.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.bogdanorzea.bakingapp.data.ReceiptsRepository;

public class DetailActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final ReceiptsRepository mRepository;
    private int mId;

    public DetailActivityViewModelFactory(ReceiptsRepository repository, int id) {
        this.mRepository = repository;
        this.mId = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailActivityViewModel(mRepository, mId);
    }
}
