package com.bogdanorzea.bakingapp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogdanorzea.bakingapp.R;
import com.bogdanorzea.bakingapp.data.database.Recipe;
import com.bogdanorzea.bakingapp.ui.detail.StepListActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {
    private final Context context;
    private List<Recipe> recipes;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Recipe recipe = (Recipe) v.getTag();
            Timber.d("Recipe at position %s was clicked", recipe.getId());

            Intent intent = new Intent(context, StepListActivity.class);
            intent.putExtra(StepListActivity.RECIPE_ID, recipe.getId());

            context.startActivity(intent);
        }
    };

    RecipesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.activity_recipe_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe currentRecipe = recipes.get(position);

        holder.recipeName.setText(currentRecipe.getName());
        String imagePath = currentRecipe.getImage();
        if (!TextUtils.isEmpty(imagePath)) {
            Picasso.get().load(imagePath)
                    .error(R.drawable.cookie)
                    .into(holder.recipeImage);
        } else {
            Picasso.get().load(R.drawable.cookie)
                    .into(holder.recipeImage);
        }

        holder.itemView.setOnClickListener(onClickListener);
        holder.itemView.setTag(currentRecipe);
    }

    @Override
    public int getItemCount() {
        if (recipes == null) return 0;

        return recipes.size();
    }

    public void swapRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipe_name)
        TextView recipeName;
        @BindView(R.id.recipe_image)
        ImageView recipeImage;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
