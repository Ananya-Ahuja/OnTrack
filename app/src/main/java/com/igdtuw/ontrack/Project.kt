package com.igdtuw.ontrack

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val startDate: LocalDate = LocalDate.now(),
    val isCompleted: Boolean = false
)
