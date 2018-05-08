package com.bogdanorzea.bakingapp.ui.detail.recipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bogdanorzea.bakingapp.data.database.Recipe;
import com.bogdanorzea.bakingapp.data.database.Step;

class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int INGREDIENT_TYPE = 0;
    private static final int STEP_TYPE = 1;
    private final Context context;
    private Recipe recipe;

    public RecipeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return INGREDIENT_TYPE;

        return STEP_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == INGREDIENT_TYPE) {
            ((ViewHolder) holder).mText.setText(recipe.getStringIngredients());
        } else {
            Step currentStep = recipe.getSteps().get(position - 1);
            ((ViewHolder) holder).mText.setText(currentStep.getShortDescription());
        }
    }

    @Override
    public int getItemCount() {
        if (recipe == null) return 0;

        return (recipe.getSteps().size() + 1);
    }

    public void swapIngredients(Recipe recipe) {
        this.recipe = recipe;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mText;

        ViewHolder(View itemView) {
            super(itemView);

            mText = itemView.findViewById(android.R.id.text1);
        }
    }
}
