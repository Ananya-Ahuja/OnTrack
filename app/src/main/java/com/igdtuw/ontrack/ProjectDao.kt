package com.igdtuw.ontrack

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Insert
    suspend fun insertProject(project: Project): Long

    @Insert
    suspend fun insertMilestone(milestone: Milestone)

    @Update
    suspend fun updateMilestone(milestone: Milestone)

    @Transaction
    @Query("SELECT * FROM projects")
    fun getProjectsWithMilestones(): Flow<List<ProjectWithMilestones>>

    @Query("DELETE FROM projects WHERE id = :projectId")
    suspend fun deleteProject(projectId: Long)
}
