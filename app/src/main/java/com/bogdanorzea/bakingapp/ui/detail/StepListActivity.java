package com.bogdanorzea.bakingapp.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.bogdanorzea.bakingapp.InjectorUtils;
import com.bogdanorzea.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.bogdanorzea.bakingapp.ui.detail.StepFragmentUtils.replaceFragmentInActivity;

public class StepListActivity extends AppCompatActivity
        implements StepDetailFragment.OnStepNavigationCallback {

    public static final String RECIPE_ID = "RECIPE_ID";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.step_list)
    RecyclerView recyclerView;

    private RecipeViewModel viewModel;
    private int recipeId = -1;
    private boolean isTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // If this view is present, then the activity should be in two-pane mode.
        isTwoPane = findViewById(R.id.step_details_container) != null;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        StepAdapter adapter = new StepAdapter(this, isTwoPane);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(RECIPE_ID)) {
            recipeId = intent.getIntExtra(RECIPE_ID, -1);

            if (recipeId != -1) {
                RecipeViewModelFactory factory = InjectorUtils.provideDetailViewModelFactory(this, recipeId);
                viewModel = ViewModelProviders.of(this, factory).get(RecipeViewModel.class);
                viewModel.getRecipe().observe(this, recipe -> {
                    if (recipe != null) {
                        setTitle(recipe.getName());

                        ((TextView) findViewById(R.id.ingredients_text))
                                .setText(recipe.getStringIngredients());

                        adapter.swapSteps(recipe.steps);
                    }
                });
            } else {
                Timber.e("Invalid recipe id");
                Toast.makeText(this, "Invalid recipe id", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void replaceStepFragment(int stepId) {
        replaceFragmentInActivity(this, recipeId, stepId);
    }
}
