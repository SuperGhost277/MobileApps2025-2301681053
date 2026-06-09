package com.example.mobileapps2025_2301681053.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Това казва на Android, че искаме таблица с име travel_table
@Entity(tableName = "travel_table")
data class Travel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Автоматично ID за всеки запис
    val title: String,                                // Заглавие на пътуването
    val note: String,                                 // Описание/бележка
    val imagePath: String? = null                     // Път към снимка (може да е празно)
)