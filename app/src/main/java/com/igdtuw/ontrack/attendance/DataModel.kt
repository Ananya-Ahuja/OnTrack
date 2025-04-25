package com.igdtuw.ontrack.attendance

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class AttendanceRecord(
    val date: LocalDate,
    val subject: String,
    val professor: String,
    val room: String,
    val time: String,
    val attended: Boolean
)

data class ClassInfo(
    val subject: String,
    val professor: String,
    val time: String,
    val room: String,
    val attendancePercentage: Int
)

class AttendanceRepository(private val context: Context) {
    private val gson = Gson()
    private val attendanceFileName = "attendance_records.json"

    fun saveAttendanceRecord(record: AttendanceRecord) {
        val records = getAttendanceRecords().toMutableList()

        // Check if record already exists for this date and subject
        val existingIndex = records.indexOfFirst {
            it.date == record.date && it.subject == record.subject
        }

        if (existingIndex != -1) {
            // Update existing record
            records[existingIndex] = record
        } else {
            // Add new record
            records.add(record)
        }

        saveAttendanceRecords(records)
    }

    private fun saveAttendanceRecords(records: List<AttendanceRecord>) {
        val json = gson.toJson(records)
        val file = File(context.filesDir, attendanceFileName)
        file.writeText(json)
    }

    fun getAttendanceRecords(): List<AttendanceRecord> {
        val file = File(context.filesDir, attendanceFileName)
        if (!file.exists()) return emptyList()

        val json = file.readText()
        val type = object : TypeToken<List<AttendanceRecord>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun getAttendancePercentage(subject: String): Int {
        val records = getAttendanceRecords().filter { it.subject == subject }
        if (records.isEmpty()) return 0

        val attended = records.count { it.attended }
        return (attended * 100 / records.size)
    }

    fun getAttendanceByDate(date: LocalDate): List<AttendanceRecord> {
        return getAttendanceRecords().filter { it.date == date }
    }

    fun getSubjects(): List<String> {
        return getAttendanceRecords()
            .map { it.subject }
            .distinct()
    }
}
