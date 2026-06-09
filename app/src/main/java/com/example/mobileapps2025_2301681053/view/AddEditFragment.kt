package com.example.mobileapps2025_2301681053.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mobileapps2025_2301681053.R
import com.example.mobileapps2025_2301681053.model.TravelDatabase
import com.example.mobileapps2025_2301681053.repository.TravelRepository
import com.example.mobileapps2025_2301681053.viewmodel.TravelViewModel
import com.example.mobileapps2025_2301681053.viewmodel.TravelViewModelFactory

class AddEditFragment : Fragment(R.layout.fragment_add_edit) {

    private lateinit var viewModel: TravelViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Подготовка на Базата данни, Репозиторито и ViewModel-а
        val database = TravelDatabase.getDatabase(requireContext())
        val repository = TravelRepository(database.travelDao())
        val factory = TravelViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TravelViewModel::class.java]

        // 2. Намиране на полетата и бутона от дизайна
        val editTitle = view.findViewById<EditText>(R.id.editTextTitle)
        val editNote = view.findViewById<EditText>(R.id.editTextNote)
        val buttonSave = view.findViewById<Button>(R.id.buttonSave)

        // 3. Какво се случва при кликване на "Запази"
        buttonSave.setOnClickListener {
            val titleText = editTitle.text.toString()
            val noteText = editNote.text.toString()

            // Проверяваме дали потребителят е въвел заглавие
            if (titleText.isNotBlank()) {
                // Пращаме данните към ViewModel-а, за да ги запише
                viewModel.insert(titleText, noteText)

                // Връщаме се един екран назад (към списъка)
                findNavController().navigateUp()
            } else {
                // Показваме малко съобщение за грешка, ако полето е празно
                Toast.makeText(requireContext(), "Моля, въведете заглавие!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}