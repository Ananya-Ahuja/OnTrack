package com.igdtuw.ontrack.attendance

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.igdtuw.ontrack.TimetableStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val repository = AttendanceRepository(context)

    // UI State
    val selectedDate = mutableStateOf(LocalDate.now())
    val selectedCourse = mutableStateOf<String?>(null)
    val classes = mutableStateOf<List<ClassInfo>>(emptyList())
    val availableCourses = mutableStateOf<List<String>>(emptyList())

    init {
        loadClassesForDate(selectedDate.value)
        loadAvailableCourses()
    }

    fun setSelectedDate(date: LocalDate) {
        selectedDate.value = date
        loadClassesForDate(date)
    }

    fun setSelectedCourse(course: String?) {
        selectedCourse.value = course
        loadClassesForDate(selectedDate.value)
    }

    fun markAttendance(subject: String, professor: String, time: String, room: String, attended: Boolean) {
        val record = AttendanceRecord(
            date = selectedDate.value,
            subject = subject,
            professor = professor,
            time = time,
            room = room,
            attended = attended
        )

        repository.saveAttendanceRecord(record)
        loadClassesForDate(selectedDate.value)
    }

    private fun loadAvailableCourses() {
        // Get unique subjects from timetable
        val timetable = TimetableStorage.loadTimetable(context)
        val subjects = mutableSetOf<String>()

        timetable.forEach { (_, entries) ->
            entries.forEach { entry ->
                if (entry.isNotBlank()) {
                    parseSubjectInfo(entry)?.let { subjects.add(it) }
                }
            }
        }

        // Add subjects from attendance records too
        subjects.addAll(repository.getSubjects())

        availableCourses.value = subjects.toList()
    }

    private fun loadClassesForDate(date: LocalDate) {
        val dayOfWeek = date.dayOfWeek
        val timetable = TimetableStorage.loadTimetable(context)
        val dayClasses = timetable[dayOfWeek] ?: listOf()

        val classesList = mutableListOf<ClassInfo>()

        // Process each class for the day
        dayClasses.forEachIndexed { index, entry ->
            if (entry.isBlank()) return@forEachIndexed

            val subject = parseSubjectInfo(entry) ?: return@forEachIndexed
            val professor = parseProfessorInfo(entry) ?: "Unknown"
            val room = parseRoomInfo(entry) ?: "N/A"

            // Calculate time based on index (starting at 9:00 AM)
            val hour = 9 + index
            val time = formatTime(hour)

            // Get attendance percentage
            val percentage = repository.getAttendancePercentage(subject)

            classesList.add(ClassInfo(subject, professor, time, room, percentage))
        }

        // Filter by selected course if needed
        classes.value = if (selectedCourse.value != null) {
            classesList.filter { it.subject == selectedCourse.value }
        } else {
            classesList
        }
    }

    // Helper methods to parse timetable entries
    private fun parseSubjectInfo(entry: String): String? {
        return if (entry.contains(" - ")) {
            entry.substringBefore(" - ").trim()
        } else {
            entry.trim()
        }
    }

    private fun parseProfessorInfo(entry: String): String? {
        return if (entry.contains(" - ")) {
            val professorPart = entry.substringAfter(" - ", "").substringBefore(" (Room", "")
            if (professorPart.startsWith("Prof. ")) {
                professorPart.substring(6)
            } else {
                professorPart
            }
        } else {
            null
        }
    }

    private fun parseRoomInfo(entry: String): String? {
        val roomRegex = "Room (\\d+)".toRegex()
        val match = roomRegex.find(entry)
        return match?.groupValues?.get(1)
    }

    private fun formatTime(hour: Int): String {
        return when {
            hour < 12 -> "$hour:00 AM"
            hour == 12 -> "12:00 PM"
            else -> "${hour - 12}:00 PM"
        }
    }
}
