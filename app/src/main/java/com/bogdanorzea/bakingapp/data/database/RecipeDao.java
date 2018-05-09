package com.bogdanorzea.bakingapp.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecipeInfo recipeInfo);

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :id")
    LiveData<Recipe> getRecipeById(int id);

    @Transaction
    @Query("SELECT * FROM recipes")
    LiveData<List<Recipe>> getRecipes();
}
