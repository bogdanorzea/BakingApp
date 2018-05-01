package com.bogdanorzea.bakingapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bogdanorzea.bakingapp.InjectorUtils;
import com.bogdanorzea.bakingapp.R;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements ReceiptsAdapter.OnItemClickHandler {

    private MainActivityViewModel mViewModel;
    private ReceiptsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = findViewById(R.id.receipt_list);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mAdapter = new ReceiptsAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        MainActivityViewModelFactory factory = InjectorUtils.provideMainViewViewModelFactory(this);
        mViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);
        mViewModel.getReceipts().observe(this, newReceipts -> {
            mAdapter.swapReceipts(newReceipts);
        });
    }

    @Override
    public void onItemClick(int position) {
        Timber.d("Receipt at position %s was clicked", position);
    }
}
