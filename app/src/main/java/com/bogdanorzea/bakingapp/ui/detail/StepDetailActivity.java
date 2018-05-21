package com.bogdanorzea.bakingapp.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.bogdanorzea.bakingapp.R;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class StepDetailActivity extends AppCompatActivity
        implements StepDetailFragment.OnStepNavigationCallback {

    public static final String RECIPE_ID = "recipe_id";
    public static final String STEP_ID = "step_id";

    private int recipeId;
    private int stepId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            hideSystemUI();
            toolbar.setVisibility(View.GONE);
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(RECIPE_ID) && intent.hasExtra(STEP_ID)) {
            recipeId = intent.getIntExtra(RECIPE_ID, -1);
            stepId = intent.getIntExtra(STEP_ID, -1);
        } else {
            finish();
        }

        if (savedInstanceState == null) {
            StepDetailFragment stepActivityFragment = new StepDetailFragment();

            Bundle bundle = new Bundle();
            bundle.putInt(StepDetailFragment.RECIPE_ID, recipeId);
            bundle.putInt(StepDetailFragment.STEP_ID, stepId);
            stepActivityFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.step_details_container, stepActivityFragment, "FRAGMENT_STEP")
                    .commit();

            setTitle("Step " + stepId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, StepListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void replaceStepFragment(int stepId) {
        this.stepId = stepId;

        StepDetailFragment stepActivityFragment = new StepDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(StepDetailFragment.RECIPE_ID, recipeId);
        bundle.putInt(StepDetailFragment.STEP_ID, this.stepId);
        stepActivityFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_details_container, stepActivityFragment, "FRAGMENT_STEP")
                .commit();

        setTitle("Step " + stepId);
    }
}
