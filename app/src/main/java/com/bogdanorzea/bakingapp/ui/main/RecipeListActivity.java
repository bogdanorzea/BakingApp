package com.bogdanorzea.bakingapp.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.bogdanorzea.bakingapp.InjectorUtils;
import com.bogdanorzea.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recipe_list)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (getResources().getConfiguration().smallestScreenWidthDp >= 600) {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
        }

        RecipesAdapter adapter = new RecipesAdapter(this);
        recyclerView.setAdapter(adapter);

        RecipeListViewModelFactory factory = InjectorUtils.provideMainViewViewModelFactory(this);
        RecipeListViewModel viewModel = ViewModelProviders.of(this, factory)
                .get(RecipeListViewModel.class);
        viewModel.getRecipes().observe(this, newRecipes -> adapter.swapRecipes(newRecipes));
    }

}
