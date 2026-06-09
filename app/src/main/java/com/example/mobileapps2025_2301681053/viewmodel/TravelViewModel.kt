package com.example.mobileapps2025_2301681053.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobileapps2025_2301681053.model.Travel
import com.example.mobileapps2025_2301681053.repository.TravelRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow

class TravelViewModel(private val repository: TravelRepository) : ViewModel() {

    // Данните, които екранът (UI) ще наблюдава и показва
    val allTravels: Flow<List<Travel>> = repository.allTravels

    // Функция, която ще се извика, когато потребителят натисне бутона "Запази"
    fun insert(title: String, note: String, imagePath: String?) {
        // Вече подаваме и пътя до снимката (imagePath)
        val newTravel = Travel(title = title, note = note, imagePath = imagePath)

        viewModelScope.launch {
            repository.insert(newTravel)
        }
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

// Тази "Фабрика" е задължителна в Android, когато ViewModel-ът ни
// приема параметър (в нашия случай приема TravelRepository)
class TravelViewModelFactory(private val repository: TravelRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TravelViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TravelViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}