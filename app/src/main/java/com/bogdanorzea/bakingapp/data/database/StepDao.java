package com.bogdanorzea.bakingapp.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface StepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(List<Step> stepEntities);

    @Query("SELECT * FROM steps WHERE recipeId = :recipeId")
    LiveData<List<Step>> getStepsByRecipeId(int recipeId);

    @Query("SELECT * FROM steps WHERE recipeId = :recipeId AND id = :stepId")
    LiveData<Step> getStepsByRecipeIdStepId(int recipeId, int stepId);
}
