package com.bogdanorzea.bakingapp.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.bogdanorzea.bakingapp.data.ReceiptsRepository;

public class MainActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final ReceiptsRepository mRepository;

    public MainActivityViewModelFactory(ReceiptsRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainActivityViewModel(mRepository);
    }
}
