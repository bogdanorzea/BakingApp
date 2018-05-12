package com.bogdanorzea.bakingapp.ui.detail.recipe;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bogdanorzea.bakingapp.InjectorUtils;
import com.bogdanorzea.bakingapp.R;
import com.bogdanorzea.bakingapp.ui.detail.step.StepActivity;

import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeActivityFragment extends Fragment
        implements StepAdapter.OnItemClickHandler {

    public static final String RECIPE_ID = "RECIPE_ID";
    private RecipeActivityViewModel viewModel;
    private int recipeId;

    public RecipeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        Context context = getContext();


        RecyclerView recyclerView = view.findViewById(R.id.recipe_content_list);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        StepAdapter adapter = new StepAdapter(context, this);
        recyclerView.setAdapter(adapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            recipeId = bundle.getInt(RECIPE_ID, -1);

            Toast.makeText(getContext(), "" + recipeId, Toast.LENGTH_SHORT).show();
        }

        if (recipeId != -1) {
            RecipeActivityViewModelFactory factory = InjectorUtils.provideDetailViewModelFactory(context, recipeId);
            viewModel = ViewModelProviders.of(this, factory).get(RecipeActivityViewModel.class);
            viewModel.getRecipe().observe(this, newRecipe -> {
                adapter.swapSteps(newRecipe.steps);
            });
        }

        return view;
    }

    @Override
    public void onItemClick(int stepId) {
        Timber.d("Step at position %s was clicked", stepId);
        Context context = getContext();

        Intent intent = new Intent(context, StepActivity.class);
        intent.putExtra(StepActivity.RECIPE_ID, recipeId);
        intent.putExtra(StepActivity.STEP_ID, stepId);

        startActivity(intent);
    }
}
