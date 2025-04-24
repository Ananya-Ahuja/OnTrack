package com.igdtuw.ontrack

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.time.DayOfWeek

object TimetableStorage {
    private const val FILENAME = "timetable.json"

    fun saveTimetable(context: Context, timetable: Map<DayOfWeek, List<String>>) {
        val gson = Gson()
        val json = gson.toJson(timetable)
        val file = File(context.filesDir, FILENAME)
        file.writeText(json)
    }

    fun loadTimetable(context: Context): Map<DayOfWeek, List<String>> {
        val file = File(context.filesDir, FILENAME)
        if (!file.exists()) return emptyMap()
        val json = file.readText()
        val type = object : TypeToken<Map<DayOfWeek, List<String>>>() {}.type
        return Gson().fromJson(json, type)
    }
}