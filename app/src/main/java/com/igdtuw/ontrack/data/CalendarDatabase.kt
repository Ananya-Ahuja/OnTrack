package com.igdtuw.ontrack.data

import android.content.Context
import androidx.room.*
import androidx.room.RoomDatabase

@Database(entities = [EventEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class CalendarDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile private var INSTANCE: CalendarDatabase? = null

        fun getDatabase(context: Context): CalendarDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    CalendarDatabase::class.java,
                    "calendar_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
