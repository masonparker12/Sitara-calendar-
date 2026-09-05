package com.example.data

import kotlinx.coroutines.flow.Flow

class EventRepository(private val eventDao: EventDao) {
    fun getEventsForDate(date: String): Flow<List<EventEntity>> = eventDao.getEventsForDate(date)

    fun getAllEvents(): Flow<List<EventEntity>> = eventDao.getAllEvents()

    suspend fun insertEvent(event: EventEntity): Long = eventDao.insertEvent(event)

    suspend fun deleteEvent(event: EventEntity) = eventDao.deleteEvent(event)
}
