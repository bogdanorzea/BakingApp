package com.bogdanorzea.bakingapp.ui.detail;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.bogdanorzea.bakingapp.R;

public class StepFragmentUtils {
    static void replaceFragmentInActivity(AppCompatActivity activity, int recipeId, int stepId) {
        StepDetailFragment stepActivityFragment = new StepDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(StepDetailFragment.RECIPE_ID, recipeId);
        bundle.putInt(StepDetailFragment.STEP_ID, stepId);
        stepActivityFragment.setArguments(bundle);

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_details_container, stepActivityFragment, "FRAGMENT_STEP")
                .commit();
    }
}
