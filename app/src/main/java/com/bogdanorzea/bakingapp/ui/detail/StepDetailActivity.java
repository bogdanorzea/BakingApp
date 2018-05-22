package com.bogdanorzea.bakingapp.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.bogdanorzea.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static com.bogdanorzea.bakingapp.ui.detail.StepFragmentUtils.replaceFragmentInActivity;

public class StepDetailActivity extends AppCompatActivity
        implements StepDetailFragment.OnStepNavigationCallback {

    public static final String RECIPE_ID = "recipe_id";
    public static final String STEP_ID = "step_id";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private int recipeId;
    private int stepId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);

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
            replaceFragmentInActivity(this, recipeId, stepId);
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

        replaceFragmentInActivity(this, recipeId, stepId);
        setTitle("Step " + stepId);
    }
}
