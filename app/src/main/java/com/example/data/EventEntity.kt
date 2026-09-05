package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val date: String, // Format: YYYY-MM-DD
    val time: String, // Format: e.g. "09:30 AM"
    val description: String = "",
    val colorHex: String = "#8E24AA"
)
