package com.fabianbleile.bakeryreloaded.Room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

@Database(entities = IngredientWidget.class, version = 1, exportSchema = false)
public abstract class IngredientDatabase extends RoomDatabase {

    private static IngredientDatabase INSTANCE;

    public static IngredientDatabase getDatabase(Context context) {
        if (INSTANCE == null) {

            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), IngredientDatabase.class, "ingredients_database")
                            .build();
        }
        return INSTANCE;
    }

    public abstract IngredientDao contactDao();
}
