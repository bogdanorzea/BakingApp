package com.bogdanorzea.bakingapp.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bogdanorzea.bakingapp.data.database.Recipe;

import java.util.List;

class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {
    private final Context mContext;
    private final OnItemClickHandler mClickHandler;
    private List<Recipe> mRecipes;

    public RecipesAdapter(Context context, OnItemClickHandler onItemClickHandler) {
        mContext = context;
        mClickHandler = onItemClickHandler;
    }

    @NonNull
    @Override
    public RecipesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(android.R.layout.simple_list_item_1, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesAdapter.ViewHolder holder, int position) {
        Recipe currentRecipe = mRecipes.get(position);

        holder.mText.setText(currentRecipe.getName());
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) return 0;

        return mRecipes.size();
    }

    public void swapRecipes(List<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    public interface OnItemClickHandler {
        void onItemClick(int recipeId);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mText;

        ViewHolder(View itemView) {
            super(itemView);

            mText = itemView.findViewById(android.R.id.text1);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onItemClick(mRecipes.get(getAdapterPosition()).getId());
        }
    }
}
