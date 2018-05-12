package com.bogdanorzea.bakingapp.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.bogdanorzea.bakingapp.R;

import timber.log.Timber;

public class RecipeActivity extends AppCompatActivity
        implements StepAdapter.OnStepItemClickHandler {

    public static final String RECIPE_ID = "RECIPE_ID";
    private int mId = -1;

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
        if (intent != null && intent.hasExtra(RECIPE_ID)) {
            mId = intent.getIntExtra(RECIPE_ID, -1);
        } else {
            finish();
        }

        if (savedInstanceState == null) {
            RecipeActivityFragment recipeActivityFragment = new RecipeActivityFragment();
            recipeActivityFragment.setRecipeId(mId);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_recipe_details, recipeActivityFragment, "FRAGMENT_RECIPE")
                    .commit();
        }
    }

    @Override
    public void onStepItemClick(int stepId) {
        Timber.d("Step at position %s was clicked", stepId);

        StepActivityFragment stepActivityFragment = new StepActivityFragment();
        stepActivityFragment.setRecipeId(mId);
        stepActivityFragment.setStepId(stepId);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_recipe_details, stepActivityFragment, "FRAGMENT_STEP")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();

        if (backStackCount >= 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
