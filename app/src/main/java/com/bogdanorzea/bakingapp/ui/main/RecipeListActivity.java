package com.bogdanorzea.bakingapp.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bogdanorzea.bakingapp.InjectorUtils;
import com.bogdanorzea.bakingapp.R;
import com.bogdanorzea.bakingapp.data.database.Recipe;
import com.bogdanorzea.bakingapp.ui.detail.StepListActivity;

import java.util.List;

import timber.log.Timber;

public class RecipeListActivity extends AppCompatActivity {

    private RecipeListViewModel mViewModel;
    private RecipesAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        RecyclerView mRecyclerView = findViewById(R.id.recipe_list);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mAdapter = new RecipesAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        RecipeListViewModelFactory factory = InjectorUtils.provideMainViewViewModelFactory(this);
        mViewModel = ViewModelProviders.of(this, factory).get(RecipeListViewModel.class);
        mViewModel.getRecipes().observe(this, newRecipes -> {
            mAdapter.swapRecipes(newRecipes);
        });
    }

    static class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {
        private final Context mContext;
        private List<Recipe> mRecipes;
        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe recipe = (Recipe) v.getTag();
                Timber.d("Recipe at position %s was clicked", recipe.getId());

                Intent intent = new Intent(mContext, StepListActivity.class);
                intent.putExtra(StepListActivity.RECIPE_ID, recipe.getId());

                mContext.startActivity(intent);
            }
        };

        RecipesAdapter(Context context) {
            mContext = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext)
                    .inflate(android.R.layout.simple_list_item_1, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Recipe currentRecipe = mRecipes.get(position);

            holder.mText.setText(currentRecipe.getName());
            holder.itemView.setOnClickListener(onClickListener);
            holder.itemView.setTag(currentRecipe);
        }

        @Override
        public int getItemCount() {
            if (mRecipes == null) return 0;

            return mRecipes.size();
        }

        void swapRecipes(List<Recipe> recipes) {
            mRecipes = recipes;
            notifyDataSetChanged();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView mText;

            ViewHolder(View itemView) {
                super(itemView);

                mText = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
