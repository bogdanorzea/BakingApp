package com.bogdanorzea.bakingapp.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ReceiptDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(List<Receipt> receiptEntities);

    @Query("SELECT * FROM receipts WHERE id = :id")
    LiveData<Receipt> getReceiptById(int id);

    @Query("SELECT * FROM receipts")
    LiveData<List<Receipt>> getAllReceipts();
}
