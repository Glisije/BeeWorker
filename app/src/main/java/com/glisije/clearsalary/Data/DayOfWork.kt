package com.glisije.clearsalary.Data

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.util.Calendar

@Entity(tableName = "day_of_work_table")
data class DayOfWork(
    @ColumnInfo(name = "time")
    val time: Int,

    @ColumnInfo(name = "calendar_start")
    val calendarStart: Calendar,

    @ColumnInfo(name = "calendar_stop")
    val calendarStop: Calendar,

    @ColumnInfo(name = "salary")
    val salary: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    suspend fun insertDayOfWorkToDatabase(dayOfWork: DayOfWork, context: Context?) {
        context ?: return // Handle null context if needed

        val database = MyAppDatabase.getDayOfWorkDao(context)
        database.dayOfWorkDao().insertDayOfWork(dayOfWork)
    }
}