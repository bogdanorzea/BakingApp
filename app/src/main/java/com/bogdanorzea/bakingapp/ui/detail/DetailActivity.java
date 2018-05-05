package com.bogdanorzea.bakingapp.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bogdanorzea.bakingapp.InjectorUtils;
import com.bogdanorzea.bakingapp.R;

import timber.log.Timber;

public class DetailActivity extends AppCompatActivity {

    public static final String RECIPE_ID = "RECIPE_ID";
    private int mId = -1;
    private DetailActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(RECIPE_ID)) {
                mId = intent.getIntExtra(RECIPE_ID, -1);
            }
        }

        if (mId != -1) {
            DetailActivityViewModelFactory factory = InjectorUtils.provideDetailViewModelFactory(this, mId);
            mViewModel = ViewModelProviders.of(this, factory).get(DetailActivityViewModel.class);
            mViewModel.getRecipe().observe(this, newRecipe -> {
                Timber.d(newRecipe.getName());
            });
        }
    }

}
