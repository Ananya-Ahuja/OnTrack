package com.igdtuw.ontrack

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        Project::class,
        Milestone::class,
        Task::class, Course::class],
    version = 2 // Incremented version
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun projectDao(): ProjectDao
    abstract fun courseDao(): CourseDao
}
