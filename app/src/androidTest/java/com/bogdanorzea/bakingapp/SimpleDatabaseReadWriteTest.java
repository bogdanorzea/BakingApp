package com.bogdanorzea.bakingapp;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.bogdanorzea.bakingapp.data.database.BakingDatabase;
import com.bogdanorzea.bakingapp.data.database.Ingredient;
import com.bogdanorzea.bakingapp.data.database.Recipe;
import com.bogdanorzea.bakingapp.data.database.RecipeDao;
import com.bogdanorzea.bakingapp.data.database.Step;
import com.bogdanorzea.bakingapp.util.RecipeTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static com.bogdanorzea.bakingapp.util.LiveDataTestUtil.getValue;

@RunWith(AndroidJUnit4.class)
public class SimpleDatabaseReadWriteTest {

    private BakingDatabase mDatabase;
    private RecipeDao mDao;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDatabase = Room.inMemoryDatabaseBuilder(context, BakingDatabase.class).build();
        mDao = mDatabase.getRecipeDao();
    }

    @Test
    public void writeRecipeAndReadList() throws InterruptedException {
        final int ID = 1;

        Recipe recipe = new Recipe(ID, "Test Recipe", 0, null);
        Ingredient ingredient = new Ingredient(1.0, "ml", "vanilla");
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient);
        recipe.setIngredients(ingredients);

        Step step = new Step();
        step.setId(1);
        step.setDescription("Long description");
        step.setShortDescription("Short description");
        List<Step> steps = new ArrayList<>();
        steps.add(step);
        recipe.setSteps(steps);

        mDao.insert(recipe);

        Recipe recipeById = getValue(mDao.getRecipeById(ID));
        RecipeTestUtil.assertEquals(recipe, recipeById);
    }


    @After
    public void closeDb() {
        mDatabase.close();
    }
}
