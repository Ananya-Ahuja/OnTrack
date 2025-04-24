package com.igdtuw.timetable

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.igdtuw.ontrack.TimetableStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.DayOfWeek
import javax.inject.Inject

@HiltViewModel
class UploadTimetableViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val context: Context = application.applicationContext
    var timetable by mutableStateOf(
        mutableMapOf(
            DayOfWeek.MONDAY to MutableList(9) { "" },
            DayOfWeek.TUESDAY to MutableList(9) { "" },
            DayOfWeek.WEDNESDAY to MutableList(9) { "" },
            DayOfWeek.THURSDAY to MutableList(9) { "" },
            DayOfWeek.FRIDAY to MutableList(9) { "" },
            DayOfWeek.SATURDAY to MutableList(9) { "" },
            DayOfWeek.SUNDAY to MutableList(9) { "" }
        )
    )
        private set

    fun updateTimeSlot(day: DayOfWeek, hourIndex: Int, value: String) {
        timetable[day]?.let { daySlots ->
            daySlots[hourIndex] = value
            timetable = timetable.toMutableMap() // Trigger recomposition
        }
    }

    fun saveTimetable(context: Context) {
        TimetableStorage.saveTimetable(context, timetable)
    }
}