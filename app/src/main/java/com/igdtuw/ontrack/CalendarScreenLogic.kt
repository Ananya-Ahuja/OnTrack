// CalendarScreenLogic.kt (Single Source of Truth)
package com.igdtuw.ontrack

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.AndroidViewModel
import android.app.Application
import javax.inject.Inject
import kotlin.text.Typography.dagger

@HiltViewModel
class CalendarScreenLogic @Inject constructor(
    application: Application  // Use ApplicationContext instead of Context
) : AndroidViewModel(application) {  // Extend AndroidViewModel

    private val context: Context = application.applicationContext
    private val _state = MutableStateFlow(CalendarState())
    val state: StateFlow<CalendarState> = _state.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch(Dispatchers.IO) {  // Use background thread
            val loadedTimetable = TimetableStorage.loadTimetable(context)
            val loadedEvents = TaskStorage.loadTasks(context)

            _state.update {
                it.copy(
                    timetable = loadedTimetable,
                    events = loadedEvents,
                    currentTasks = loadedEvents[it.selectedDate] ?: mutableListOf()
                )
            }
        }
    }

    // Month navigation handlers
    fun onMonthNavigationForward() {
        _state.update { state ->
            state.copy(visibleMonth = state.visibleMonth.plusMonths(1))
        }
    }

    fun onMonthNavigationBackward() {
        _state.update { state ->
            state.copy(visibleMonth = state.visibleMonth.minusMonths(1))
        }
    }

    // Date selection handler
    fun onDateSelected(newDate: LocalDate) {
        _state.update { state ->
            state.copy(
                selectedDate = newDate,
                currentTasks = updateTasksForDate(newDate, state)
            )
        }
    }

    private fun updateTasksForDate(date: LocalDate, state: CalendarState): MutableList<String> {
        return state.events[date] ?: run {
            val timetableEntries = state.timetable[date.dayOfWeek]
                ?.filter { it.isNotBlank() }?.toMutableList() ?: mutableListOf()

            _state.update { s ->
                s.copy(events = s.events + (date to timetableEntries))
            }
            timetableEntries
        }
    }

    // Task management
    fun toggleDialog() {
        _state.update { it.copy(showDialog = !it.showDialog) }
    }

    fun addTask(task: String) {
        viewModelScope.launch(Dispatchers.IO) {  // Background thread
            val newEvents = _state.value.events.toMutableMap().apply {
                val tasks = getOrDefault(_state.value.selectedDate, mutableListOf())
                put(_state.value.selectedDate, tasks.apply { add(task) })
            }

            _state.update {
                it.copy(
                    events = newEvents,
                    currentTasks = newEvents[it.selectedDate] ?: mutableListOf(),
                    showDialog = false
                )
            }
            TaskStorage.saveTasks(context, newEvents)
        }
    }

    fun deleteTask(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {  // Background thread
            val newEvents = _state.value.events.toMutableMap().apply {
                _state.value.currentTasks.toMutableList().apply {
                    removeAt(index)
                }.also { updatedTasks ->
                    put(_state.value.selectedDate, updatedTasks)
                }
            }

            _state.update {
                it.copy(
                    events = newEvents,
                    currentTasks = newEvents[it.selectedDate] ?: mutableListOf()
                )
            }
            TaskStorage.saveTasks(context, newEvents)
        }
    }
}

data class CalendarState(
    val selectedDate: LocalDate = LocalDate.now(),
    val visibleMonth: LocalDate = LocalDate.now().withDayOfMonth(1),
    val currentTasks: MutableList<String> = mutableListOf(),
    val events: Map<LocalDate, MutableList<String>> = emptyMap(),
    val timetable: Map<DayOfWeek, List<String>> = emptyMap(),
    val showDialog: Boolean = false
)
