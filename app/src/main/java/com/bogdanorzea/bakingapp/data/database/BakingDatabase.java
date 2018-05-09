package com.bogdanorzea.bakingapp.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1, exportSchema = false)
public abstract class BakingDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "recipes";
    private static final Object LOCK = new Object();
    private static volatile BakingDatabase sInstance;

    public static BakingDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            BakingDatabase.class,
                            BakingDatabase.DATABASE_NAME).build();
                }
            }
        }

        return sInstance;
    }

    public abstract RecipeDao getRecipeDao();

    public abstract IngredientDao getIngredientDao();

    public abstract StepDao getStepDao();
}
