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
    fun insert(title: String, note: String) {
        // Създаваме нов обект Travel
        val newTravel = Travel(title = title, note = note)

        // Стартираме запазването в базата данни на заден фон (чрез viewModelScope)
        // Това гарантира, че приложението няма да забие ("замръзне"), докато записва
        viewModelScope.launch {
            repository.insert(newTravel)
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