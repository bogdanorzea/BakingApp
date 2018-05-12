package com.bogdanorzea.bakingapp.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bogdanorzea.bakingapp.InjectorUtils;
import com.bogdanorzea.bakingapp.R;

import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeActivityFragment extends Fragment {

    public static final String RECIPE_ID = "RECIPE_ID";
    private RecipeActivityViewModel viewModel;
    private int recipeId = -1;
    private StepAdapter.OnStepItemClickHandler onStepItemClickListener;

    public RecipeActivityFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onStepItemClickListener = (StepAdapter.OnStepItemClickHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnStepItemClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        Context context = getContext();


        RecyclerView recyclerView = view.findViewById(R.id.recipe_content_list);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        StepAdapter adapter = new StepAdapter(context, onStepItemClickListener);
        recyclerView.setAdapter(adapter);

        if (savedInstanceState != null) {
            recipeId = savedInstanceState.getInt(RECIPE_ID);
        }

        if (recipeId != -1) {
            RecipeActivityViewModelFactory factory = InjectorUtils.provideDetailViewModelFactory(context, recipeId);
            viewModel = ViewModelProviders.of(this, factory).get(RecipeActivityViewModel.class);
            viewModel.getRecipe().observe(this, newRecipe -> {
                adapter.swapSteps(newRecipe.steps);
            });
        } else {
            Timber.e("Invalid recipe id");
            Toast.makeText(context, "Invalid recipe id", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RECIPE_ID, recipeId);
    }
}
