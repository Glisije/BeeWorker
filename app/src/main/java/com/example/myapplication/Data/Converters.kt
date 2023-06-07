package com.glisije.clearsalary.Data

import java.util.Calendar
import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun calendarToLong(calendar: Calendar): Long {
        return calendar.timeInMillis
    }

    @TypeConverter
    fun longToCalendar(value: Long): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = value
        return calendar
    }
}
