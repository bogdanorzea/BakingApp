package com.bogdanorzea.bakingapp.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bogdanorzea.bakingapp.InjectorUtils;
import com.bogdanorzea.bakingapp.R;
import com.bogdanorzea.bakingapp.ui.detail.recipe.RecipeActivity;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.OnItemClickHandler {

    private MainActivityViewModel mViewModel;
    private RecipesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = findViewById(R.id.recipe_list);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mAdapter = new RecipesAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        MainActivityViewModelFactory factory = InjectorUtils.provideMainViewViewModelFactory(this);
        mViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);
        mViewModel.getRecipes().observe(this, newRecipes -> {
            mAdapter.swapRecipes(newRecipes);
        });
    }

    @Override
    public void onItemClick(int recipeId) {
        Timber.d("Recipe at position %s was clicked", recipeId);

        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(RecipeActivity.RECIPE_ID, recipeId);

        startActivity(intent);
    }
}
