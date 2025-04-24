package com.igdtuw.ontrack

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.time.LocalDate

object TaskStorage {

    private const val PREF_NAME = "task_prefs"
    private const val TASKS_KEY = "tasks"

    // Save tasks to SharedPreferences
    fun saveTasks(context: Context, tasks: Map<LocalDate, MutableList<String>>) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert the tasks map to JSON
        val gson = Gson()
        val tasksJson = gson.toJson(tasks)

        Log.d("TaskStorage", "Saving tasks: $tasksJson")  // Debugging log

        editor.putString(TASKS_KEY, tasksJson)
        editor.apply()  // Save changes
    }

    // Load tasks from SharedPreferences
    fun loadTasks(context: Context): Map<LocalDate, MutableList<String>> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val tasksJson = sharedPreferences.getString(TASKS_KEY, "") ?: ""

        Log.d("TaskStorage", "Loading tasks: $tasksJson")  // Debugging log

        return if (tasksJson.isNotEmpty()) {
            // Convert JSON string back to Map
            val gson = Gson()
            val type: Type = object : TypeToken<Map<LocalDate, MutableList<String>>>() {}.type
            gson.fromJson(tasksJson, type)
        } else {
            emptyMap()
        }
    }
}