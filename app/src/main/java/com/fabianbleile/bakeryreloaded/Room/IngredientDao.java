package com.fabianbleile.bakeryreloaded.Room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface IngredientDao {
    @Query("SELECT * FROM IngredientWidget")
    List<IngredientWidget> getAll();

    @Query("SELECT * FROM IngredientWidget WHERE id IN (:contactIds)")
    List<IngredientWidget> loadAllByIds(int[] contactIds);

    @Query("SELECT * FROM IngredientWidget WHERE measure LIKE :first AND "
            + "quantity LIKE :last LIMIT 1")

    IngredientWidget findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertAll(List<IngredientWidget> ingredients);

    //(onConflict = OnConflictStrategy.REPLACE)
    @Insert
    void insertContact(IngredientWidget ingredient);

    @Delete
    void delete(IngredientWidget ingredient);
}
