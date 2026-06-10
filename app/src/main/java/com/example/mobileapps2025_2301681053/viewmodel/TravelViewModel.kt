package com.example.mobileapps2025_2301681053.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobileapps2025_2301681053.model.Travel
import com.example.mobileapps2025_2301681053.repository.TravelRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow

class TravelViewModel(private val repository: TravelRepository) : ViewModel() {

    val allTravels: Flow<List<Travel>> = repository.allTravels

    fun insert(title: String, note: String, imagePath: String?, lat: Double?, lon: Double?) {
        val newTravel = Travel(title = title, note = note, imagePath = imagePath, latitude = lat, longitude = lon)
        viewModelScope.launch { repository.insert(newTravel) }
    }
    fun update(travel: Travel) {
        viewModelScope.launch {
            repository.update(travel)
        }
    }

    fun delete(travel: Travel) {
        viewModelScope.launch {
            repository.delete(travel)
        }
    }
}

class TravelViewModelFactory(private val repository: TravelRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TravelViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TravelViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}