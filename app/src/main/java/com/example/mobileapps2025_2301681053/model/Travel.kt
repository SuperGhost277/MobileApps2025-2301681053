package com.example.mobileapps2025_2301681053.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Това казва на Android, че искаме таблица с име travel_table
@Entity(tableName = "travel_table")
data class Travel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val note: String,
    val imagePath: String? = null,
    // НОВО: Добавяме координати (може да са празни, ако потребителят не избере локация)
    val latitude: Double? = null,
    val longitude: Double? = null
)