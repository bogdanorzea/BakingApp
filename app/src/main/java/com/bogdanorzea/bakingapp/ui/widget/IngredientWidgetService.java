package com.bogdanorzea.bakingapp.ui.widget;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.bogdanorzea.bakingapp.ui.main.RecipeListViewModel;
import com.bogdanorzea.bakingapp.ui.main.RecipeListViewModelFactory;

import java.util.List;

import timber.log.Timber;

public class IngredientWidgetService extends AppCompatActivity {

    private static final String PREFS_NAME = "com.bogdanorzea.bakingapp.ui.widget.DummyWidget";
    private static final String PREF_PREFIX_KEY = "ingredient_widget#";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public IngredientWidgetService() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveRecipeIdPref(Context context, int appWidgetId, int recipeId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, recipeId);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    public static int loadRecipeIdPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);

        return prefs.getInt(PREF_PREFIX_KEY + appWidgetId, -1);
    }

    static void deleteRecipeIdPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.ingredients_widget_configure);

        RecyclerView mRecyclerView = findViewById(R.id.recipe_list);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        RecipesWidgetAdapter adapter = new RecipesWidgetAdapter(this);
        mRecyclerView.setAdapter(adapter);

        setTitle("Select recipe to display ingredients");

        RecipeListViewModelFactory factory = InjectorUtils.provideMainViewViewModelFactory(this);
        RecipeListViewModel mViewModel = ViewModelProviders.of(this, factory).get(RecipeListViewModel.class);
        mViewModel.getRecipes().observe(this, newRecipes -> {
            adapter.swapRecipes(newRecipes);
        });

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }


    public class RecipesWidgetAdapter extends RecyclerView.Adapter<RecipesWidgetAdapter.ViewHolder> {
        private final Context mContext;
        private List<Recipe> mRecipes;
        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe recipe = (Recipe) v.getTag();
                Timber.d("Recipe at position %s was clicked", recipe.getId());

                // When the button is clicked, store the string locally
                //String widgetText = mAppWidgetText.getText().toString();
                saveRecipeIdPref(mContext, mAppWidgetId, recipe.getId());

                // It is the responsibility of the configuration activity to update the app widget
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
                IngredientsWidgetProvider.updateAppWidget(mContext, appWidgetManager, mAppWidgetId);

                // Make sure we pass back the original appWidgetId
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();

            }
        };

        RecipesWidgetAdapter(Context context) {
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

        class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView mText;

            ViewHolder(View itemView) {
                super(itemView);

                mText = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
