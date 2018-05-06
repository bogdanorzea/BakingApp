package com.bogdanorzea.bakingapp.data.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class IngredientListDataTypeConverter {
    private static Gson gson = new Gson();
    private static Type type = new TypeToken<List<Ingredient>>() {
    }.getType();

    @TypeConverter
    public static List<Ingredient> stringToList(String json) {
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String listToString(List<Ingredient> ingredients) {
        return gson.toJson(ingredients, type);
    }
}