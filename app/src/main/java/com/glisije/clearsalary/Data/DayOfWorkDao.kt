package com.glisije.clearsalary.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DayOfWorkDao {
    @Query("SELECT * FROM day_of_work_table")
    fun getAllDaysOfWork(): List<DayOfWork>

    @Query("SELECT * FROM day_of_work_table WHERE id = :id")
    fun getDayOfWorkById(id: Long): DayOfWork?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDayOfWork(dayOfWork: DayOfWork): Long

    @Update
    fun updateDayOfWork(dayOfWork: DayOfWork): Int

    @Delete
    fun deleteDayOfWork(dayOfWork: DayOfWork): Int

    @Query("DELETE FROM money_for_time_table")
    fun deleteAllFromTable()
}




