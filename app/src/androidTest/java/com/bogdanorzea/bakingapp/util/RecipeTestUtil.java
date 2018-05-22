package com.bogdanorzea.bakingapp.util;

import com.bogdanorzea.bakingapp.data.database.Ingredient;
import com.bogdanorzea.bakingapp.data.database.Recipe;
import com.bogdanorzea.bakingapp.data.database.Step;

import junit.framework.Assert;

import java.util.List;

import static junit.framework.Assert.assertNotNull;

public class RecipeTestUtil {
    public static void assertEquals(Recipe oldRecipe, Recipe newRecipe) {
        Assert.assertEquals(oldRecipe.getId(), newRecipe.getId());
        Assert.assertEquals(oldRecipe.getServings(), newRecipe.getServings());
        Assert.assertEquals(oldRecipe.getName(), newRecipe.getName());
        Assert.assertEquals(oldRecipe.getImage(), newRecipe.getImage());

        assertEqualIngredients(oldRecipe, newRecipe);
        assertEqualSteps(oldRecipe, newRecipe);
    }

    private static void assertEqualSteps(Recipe oldRecipe, Recipe newRecipe) {
        List<Step> oldRecipeSteps = oldRecipe.steps;
        List<Step> newRecipeSteps = newRecipe.steps;

        assertNotNull(oldRecipeSteps);
        assertNotNull(newRecipeSteps);

        Assert.assertEquals(oldRecipeSteps.size(), newRecipeSteps.size());

        for (int i = 0; i < oldRecipeSteps.size(); i++) {
            StepTestUtil.assertEquals(oldRecipeSteps.get(i), newRecipeSteps.get(i));
        }
    }

    private static void assertEqualIngredients(Recipe oldRecipe, Recipe newRecipe) {
        List<Ingredient> oldRecipeIngredients = oldRecipe.ingredients;
        List<Ingredient> newRecipeIngredients = newRecipe.ingredients;

        assertNotNull(oldRecipeIngredients);
        assertNotNull(newRecipeIngredients);

        Assert.assertEquals(oldRecipeIngredients.size(), newRecipeIngredients.size());

        for (int i = 0; i < oldRecipeIngredients.size(); i++) {
            IngredientTestUtil.assertEquals(oldRecipeIngredients.get(i), newRecipeIngredients.get(i));
        }
    }
}
