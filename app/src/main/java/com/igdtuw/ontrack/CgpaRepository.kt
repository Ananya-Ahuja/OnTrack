package com.igdtuw.ontrack

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CgpaRepository @Inject constructor(private val courseDao: CourseDao) {
    fun getAllCourses(): Flow<List<Course>> = courseDao.getAllCourses()
    suspend fun addCourse(course: Course) = courseDao.insert(course)
    suspend fun updateCourse(course: Course) = courseDao.update(course)
    suspend fun deleteCourse(course: Course) = courseDao.delete(course)
}
