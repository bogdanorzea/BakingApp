package com.bogdanorzea.bakingapp.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bogdanorzea.bakingapp.InjectorUtils;
import com.bogdanorzea.bakingapp.R;
import com.bogdanorzea.bakingapp.data.database.Ingredient;
import com.bogdanorzea.bakingapp.data.database.Step;

import java.util.List;

import timber.log.Timber;

public class StepListActivity extends AppCompatActivity {

    public static final String RECIPE_ID = "RECIPE_ID";
    private RecipeViewModel viewModel;
    private int recipeId = -1;
    private boolean isTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // If this view is present, then the activity should be in two-pane mode.
        isTwoPane = findViewById(R.id.step_details_container) != null;

        RecyclerView recyclerView = findViewById(R.id.step_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        StepListActivity.StepAdapter adapter = new StepListActivity.StepAdapter(this, isTwoPane);
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

    static class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {
        private final AppCompatActivity parentActivity;
        private final boolean isTwoPane;

        private List<Step> steps;
        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Step step = (Step) v.getTag();
                Timber.d("Step at position %s was clicked", step.getId());

                if (isTwoPane) {
                    Timber.d("Replacing fragment to show details for Step#%s", step.getId());

                    StepDetailFragment stepDetailFragment = new StepDetailFragment();

                    Bundle bundle = new Bundle();
                    bundle.putInt(StepDetailFragment.RECIPE_ID, step.getRecipeId());
                    bundle.putInt(StepDetailFragment.STEP_ID, step.getId());
                    stepDetailFragment.setArguments(bundle);

                    FragmentManager fragmentManager = parentActivity.getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.step_details_container, stepDetailFragment, "FRAGMENT_STEP")
                            .commit();

                } else {
                    Timber.d("Starting activity to show details for Step#%s", step.getId());

                    Intent intent = new Intent(parentActivity, StepDetailActivity.class);
                    intent.putExtra(StepDetailActivity.RECIPE_ID, step.getRecipeId());
                    intent.putExtra(StepDetailActivity.STEP_ID, step.getId());

                    parentActivity.startActivity(intent);
                }
            }
        };

        StepAdapter(AppCompatActivity parentActivity, boolean isTwoPane) {
            this.parentActivity = parentActivity;
            this.isTwoPane = isTwoPane;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parentActivity)
                    .inflate(R.layout.activity_step_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Step currentStep = steps.get(position);

            holder.stepName.setText(currentStep.getShortDescription());
            holder.stepId.setText(currentStep.getId().toString());

            holder.itemView.setTag(currentStep);
            holder.itemView.setOnClickListener(onClickListener);
        }

        @Override
        public int getItemCount() {
            if (steps == null) return 0;

            return steps.size();
        }

        public void swapSteps(List<Step> steps) {
            this.steps = steps;
            notifyDataSetChanged();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView stepId;
            final TextView stepName;

            ViewHolder(View itemView) {
                super(itemView);

                stepName = itemView.findViewById(R.id.step_name);
                stepId = itemView.findViewById(R.id.step_id);
            }
        }
    }

    static class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
        private final Context context;
        private List<Ingredient> ingredients;

        public IngredientAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context)
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.mText.setText(ingredients.get(position).getName());
        }

        @Override
        public int getItemCount() {
            if (ingredients == null) return 0;

            return ingredients.size();
        }

        public void swapIngredients(List<Ingredient> ingredients) {
            this.ingredients = ingredients;
            notifyDataSetChanged();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView mText;

            ViewHolder(View itemView) {
                super(itemView);

                mText = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
