package com.glisije.clearsalary.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@TypeConverters(Converters::class)
@Database(entities = [DayOfWork::class, MoneyForTime::class], version = 1, exportSchema = false)
abstract class MyAppDatabase : RoomDatabase() {
    abstract fun dayOfWorkDao(): DayOfWorkDao
    abstract fun moneyForTimeDao(): MoneyForTimeDao

    companion object {
        @Volatile
        private var INSTANCE: MyAppDatabase? = null

        fun getDayOfWorkDao(context: Context): MyAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyAppDatabase::class.java,
                    "day_of_work_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
    suspend fun insertDayOfWork(dayOfWork: DayOfWork, context: Context?) {
        context ?: return // Handle null context if needed

        val database = MyAppDatabase.getDayOfWorkDao(context)
        database.dayOfWorkDao().insertDayOfWork(dayOfWork)
    }



    suspend fun insertMoneyForTime(moneyForTime: MoneyForTime, context: Context) {
        val moneyForTimeDao = MyAppDatabase.getDayOfWorkDao(context).moneyForTimeDao()
        withContext(Dispatchers.IO) {
            moneyForTimeDao.insert(moneyForTime)
        }
    }
}

