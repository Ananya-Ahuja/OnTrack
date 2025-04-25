package com.igdtuw.ontrack

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.time.LocalDate

object TaskStorage {
    private const val PREF_NAME = "task_prefs"
    private const val TASKS_KEY = "tasks"
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()

    // Save tasks to SharedPreferences
    fun saveTasks(context: Context, tasks: Map<LocalDate, MutableList<String>>) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Convert the tasks map to JSON
        val tasksJson = gson.toJson(tasks)
        Log.d("TaskStorage", "Saving tasks: $tasksJson")

        editor.putString(TASKS_KEY, tasksJson)
        editor.apply()
    }

    // Load tasks from SharedPreferences
    fun loadTasks(context: Context): Map<LocalDate, MutableList<String>> {
        return try {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val tasksJson = sharedPreferences.getString(TASKS_KEY, "") ?: ""
            Log.d("TaskStorage", "Loading tasks: $tasksJson")

            if (tasksJson.isEmpty()) return emptyMap()

            val type: Type = object : TypeToken<Map<LocalDate, MutableList<String>>>() {}.type
            gson.fromJson(tasksJson, type) ?: emptyMap()
        } catch (e: Exception) {
            Log.e("TaskStorage", "Error loading tasks: ${e.message}")
            emptyMap()
        }
    }

    private class LocalDateAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        override fun serialize(
            src: LocalDate,
            typeOfSrc: Type,
            context: JsonSerializationContext
        ): JsonElement {
            return JsonPrimitive(src.toString())
        }

        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): LocalDate {
            return LocalDate.parse(json.asString)
        }
    }
}
