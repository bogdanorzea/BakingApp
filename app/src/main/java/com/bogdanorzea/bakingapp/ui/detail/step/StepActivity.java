package com.bogdanorzea.bakingapp.ui.detail.step;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bogdanorzea.bakingapp.R;

public class StepActivity extends AppCompatActivity {

    public static final String RECIPE_ID = "recipe_id";
    public static final String STEP_ID = "step_id";

    private int recipeId;
    private int stepId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
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
        if (intent != null && intent.hasExtra(RECIPE_ID) && intent.hasExtra(STEP_ID)) {
            recipeId = intent.getIntExtra(RECIPE_ID, -1);
            stepId = intent.getIntExtra(STEP_ID, -1);
        } else {
            finish();
        }

        if (savedInstanceState == null) {
            StepActivityFragment stepActivityFragment = new StepActivityFragment();

            Bundle bundle = new Bundle();
            bundle.putInt(StepActivityFragment.RECIPE_ID, recipeId);
            bundle.putInt(StepActivityFragment.STEP_ID, stepId);
            stepActivityFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_step_details, stepActivityFragment, "FRAGMENT_STEP")
                    .commit();
        }
    }

}
