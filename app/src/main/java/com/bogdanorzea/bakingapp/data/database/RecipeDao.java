package com.bogdanorzea.bakingapp.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Recipe recipeEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(List<Recipe> recipeEntities);

    @Query("SELECT * FROM recipes WHERE id = :id")
    LiveData<Recipe> getRecipeById(int id);

    @Query("SELECT * FROM recipes")
    LiveData<List<Recipe>> getRecipes();
}
