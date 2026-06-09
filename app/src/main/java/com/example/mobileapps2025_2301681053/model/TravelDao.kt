package com.example.mobileapps2025_2301681053.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TravelDao {
    // Команда за създаване (Create) - Оценка 4
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(travel: Travel)

    // Команда за четене (Read) - Оценка 4
    @Query("SELECT * FROM travel_table ORDER BY id DESC")
    fun getAllTravels(): Flow<List<Travel>>
}