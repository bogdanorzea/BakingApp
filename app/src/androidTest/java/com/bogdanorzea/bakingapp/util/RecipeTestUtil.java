package com.bogdanorzea.bakingapp.util;

import com.bogdanorzea.bakingapp.data.database.Ingredient;
import com.bogdanorzea.bakingapp.data.database.Recipe;

import junit.framework.Assert;

import java.util.List;

import static junit.framework.Assert.assertNotNull;

public class RecipeTestUtil {
    public static void assertEquals(Recipe oldRecipe, Recipe newRecipe) {
        Assert.assertEquals(oldRecipe.getId(), newRecipe.getId());
        Assert.assertEquals(oldRecipe.getServings(), newRecipe.getServings());
        Assert.assertEquals(oldRecipe.getName(), newRecipe.getName());
        Assert.assertEquals(oldRecipe.getImage(), newRecipe.getImage());

        List<Ingredient> oldRecipeIngredients = oldRecipe.getIngredients();
        List<Ingredient> newRecipeIngredients = newRecipe.getIngredients();

        assertNotNull(oldRecipeIngredients);
        assertNotNull(newRecipeIngredients);

        Assert.assertEquals(oldRecipeIngredients.size(), newRecipeIngredients.size());

        for (int i = 0; i < oldRecipeIngredients.size(); i++) {
            IngredientTestUtil.assertEquals(oldRecipeIngredients.get(i), newRecipeIngredients.get(i));
        }
    }
}
