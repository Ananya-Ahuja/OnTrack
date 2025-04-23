package com.igdtuw.ontrack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import com.igdtuw.ontrack.data.EventEntity
import com.igdtuw.ontrack.data.EventRepository
import com.igdtuw.ontrack.data.EventDao

class CalendarViewModel(
    private val repository: EventRepository
) : ViewModel() {

    private val _currentMonth = MutableStateFlow(YearMonth.now())
    val currentMonth: StateFlow<YearMonth> = _currentMonth

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    // Events are now fetched from Room using Flow
    val events: StateFlow<List<EventEntity>> =
        repository.getAllEvents()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun nextMonth() {
        _currentMonth.value = _currentMonth.value.plusMonths(1)
    }

    fun prevMonth() {
        _currentMonth.value = _currentMonth.value.minusMonths(1)
    }

    fun addEvent(event: EventEntity) {
        viewModelScope.launch {
            repository.insertEvent(event)
        }
    }

    fun deleteEvent(event: EventEntity) {
        viewModelScope.launch {
            repository.deleteEvent(event)
        }
    }

    fun updateEvent(event: EventEntity) {
        viewModelScope.launch {
            repository.updateEvent(event)
        }
    }

    fun getEventsForDate(date: LocalDate): Flow<List<EventEntity>> {
        return repository.getEventsByDate(date)
    }
}