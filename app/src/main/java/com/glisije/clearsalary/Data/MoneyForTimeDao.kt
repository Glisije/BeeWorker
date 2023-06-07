package com.glisije.clearsalary.Data

import android.content.Context
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Dao
interface MoneyForTimeDao {
    @Query("SELECT * FROM money_for_time_table")
    fun getAllMoneyForTime(): List<MoneyForTime>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(moneyForTime: MoneyForTime): Long

    @Delete
    fun delete(moneyForTime: MoneyForTime)

    @Query("DELETE FROM money_for_time_table WHERE id = :id")
    fun deleteById(id: Long)

    // Other DAO methods...
}
