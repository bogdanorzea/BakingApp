package com.bogdanorzea.bakingapp.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(List<Ingredient> ingredientEntities);

    @Query("SELECT * FROM ingredients WHERE recipeId = :recipeId")
    LiveData<List<Ingredient>> getIngredientsByRecipeId(int recipeId);
}
