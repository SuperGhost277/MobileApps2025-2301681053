package com.example.mobileapps2025_2301681053.repository

import com.example.mobileapps2025_2301681053.model.Travel
import com.example.mobileapps2025_2301681053.model.TravelDao
import kotlinx.coroutines.flow.Flow

// Репозиторито приема нашия DAO интерфейс като параметър
class TravelRepository(private val travelDao: TravelDao) {

    // Взимаме списъка с всички пътувания (Read)
    val allTravels: Flow<List<Travel>> = travelDao.getAllTravels()

    // Функция за запазване на ново пътуване (Create)
    // Използваме 'suspend', защото тази операция отнема време и
    // не трябва да блокира екрана на телефона
    suspend fun insert(travel: Travel) {
        travelDao.insert(travel)
    }
}