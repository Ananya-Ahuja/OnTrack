package com.igdtuw.ontrack

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Migration moved outside the object
private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Let Room auto-create tables from entities
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app-database"
        )
            .addMigrations(MIGRATION_1_2)
            .fallbackToDestructiveMigration() // For development only
            .build()
    }

    @Provides @Singleton fun provideTaskDao(database: AppDatabase): TaskDao = database.taskDao()
    @Provides @Singleton fun provideProjectDao(database: AppDatabase): ProjectDao = database.projectDao()
    @Provides @Singleton fun provideCourseDao(database: AppDatabase): CourseDao = database.courseDao()
}
