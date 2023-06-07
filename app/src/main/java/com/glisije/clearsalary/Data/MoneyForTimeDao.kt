package com.glisije.clearsalary.Data

import android.content.Context
import androidx.room.Dao
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

    // Other DAO methods...
}
