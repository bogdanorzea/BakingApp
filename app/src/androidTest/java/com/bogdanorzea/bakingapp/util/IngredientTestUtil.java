package com.bogdanorzea.bakingapp.util;

import com.bogdanorzea.bakingapp.data.database.Ingredient;

import junit.framework.Assert;

public class IngredientTestUtil {
    public static void assertEquals(Ingredient oldIngredient, Ingredient newIngredient) {
        Assert.assertEquals(oldIngredient.getQuantity(), newIngredient.getQuantity());
        Assert.assertEquals(oldIngredient.getMeasure(), newIngredient.getMeasure());
        Assert.assertEquals(oldIngredient.getName(), newIngredient.getName());
    }
}
