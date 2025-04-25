package com.igdtuw.ontrack

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val credits: Int,
    val grade: String,
    val isProjected: Boolean = false
) {
    fun gradePoints(): Double = when (grade) {
        "S" -> 10.0
        "A" -> 9.0
        "B" -> 8.0
        "C" -> 7.0
        "D" -> 6.0
        "E" -> 5.0
        "F" -> 0.0
        else -> 0.0
    }
}
