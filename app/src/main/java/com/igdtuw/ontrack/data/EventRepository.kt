package com.igdtuw.ontrack.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class EventRepository(private val eventDao: EventDao) {

    suspend fun insertEvent(event: EventEntity) {
        eventDao.insertEvent(event)
    }

    suspend fun deleteEvent(event: EventEntity) {
        eventDao.deleteEvent(event)
    }

    suspend fun updateEvent(event: EventEntity) {
        eventDao.updateEvent(event)
    }

    fun getEventsByDate(date: LocalDate): Flow<List<EventEntity>> {
        return eventDao.getEventsByDate(date)
    }

    fun getAllEvents(): Flow<List<EventEntity>> {
        return eventDao.getAllEvents()
    }
}
