package com.igdtuw.ontrack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CgpaViewModel @Inject constructor(
    private val repository: CgpaRepository
) : ViewModel() {
    private val _courses = repository.getAllCourses()
    val courses = _courses

    private val _cgpa = MutableStateFlow(0.0)
    val cgpa = _cgpa.asStateFlow()

    fun addCourse(course: Course) = viewModelScope.launch {
        repository.addCourse(course)
    }

    fun updateCourse(course: Course) = viewModelScope.launch {
        repository.updateCourse(course)
    }

    fun deleteCourse(course: Course) = viewModelScope.launch {
        repository.deleteCourse(course)
    }

    fun calculateCgpa(courses: List<Course>) {
        var totalCredits = 0
        var totalPoints = 0.0

        courses.forEach { course ->
            totalCredits += course.credits
            totalPoints += course.credits * course.gradePoints()
        }

        _cgpa.value = if (totalCredits > 0) totalPoints / totalCredits else 0.0
    }
}
