package com.bogdanorzea.bakingapp.ui.detail.recipe;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bogdanorzea.bakingapp.InjectorUtils;
import com.bogdanorzea.bakingapp.R;
import com.bogdanorzea.bakingapp.ui.detail.step.StepActivity;

import timber.log.Timber;

public class RecipeActivity extends AppCompatActivity implements StepAdapter.OnItemClickHandler {

    public static final String RECIPE_ID = "RECIPE_ID";
    private int mId = -1;
    private RecipeActivityViewModel mViewModel;

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

        RecyclerView recyclerView = findViewById(R.id.recipe_content_list);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //IngredientAdapter adapter = new IngredientAdapter(this);
        StepAdapter adapter = new StepAdapter(this, this);
        recyclerView.setAdapter(adapter);

        if (mId != -1) {
            RecipeActivityViewModelFactory factory = InjectorUtils.provideDetailViewModelFactory(this, mId);
            mViewModel = ViewModelProviders.of(this, factory).get(RecipeActivityViewModel.class);
//            mViewModel.getIngredients().observe(this, ingredients -> {
//                adapter.swapIngredients(ingredients);
//            });
            mViewModel.getRecipe().observe(this, newRecipe -> {
                adapter.swapSteps(newRecipe.steps);
            });
        }
    }

    @Override
    public void onItemClick(int stepId) {
        Timber.d("Step at position %s was clicked", stepId);

        Intent intent = new Intent(this, StepActivity.class);
        intent.putExtra(StepActivity.RECIPE_ID, mId);

        startActivity(intent);
    }
}
