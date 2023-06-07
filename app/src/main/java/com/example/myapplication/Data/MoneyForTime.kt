package com.glisije.clearsalary.Data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "money_for_time_table")
data class MoneyForTime(
    val calendarStart: Calendar,
    val calendarStop: Calendar,
    val salaryPerMinuet: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

