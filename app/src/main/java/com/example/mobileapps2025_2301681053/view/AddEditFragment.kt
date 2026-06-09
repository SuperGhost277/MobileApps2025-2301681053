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
import com.example.mobileapps2025_2301681053.model.Travel
import com.example.mobileapps2025_2301681053.model.TravelDatabase
import com.example.mobileapps2025_2301681053.repository.TravelRepository
import com.example.mobileapps2025_2301681053.viewmodel.TravelViewModel
import com.example.mobileapps2025_2301681053.viewmodel.TravelViewModelFactory

class AddEditFragment : Fragment(R.layout.fragment_add_edit) {

    private lateinit var viewModel: TravelViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = TravelDatabase.getDatabase(requireContext())
        val repository = TravelRepository(database.travelDao())
        val factory = TravelViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TravelViewModel::class.java]

        val editTitle = view.findViewById<EditText>(R.id.editTextTitle)
        val editNote = view.findViewById<EditText>(R.id.editTextNote)
        val buttonSave = view.findViewById<Button>(R.id.buttonSave)

        // ПРОМЯНАТА Е ТУК: Проверяваме дали са ни подадени данни за редакция
        val travelId = arguments?.getInt("id", -1) ?: -1
        val travelTitle = arguments?.getString("title") ?: ""
        val travelNote = arguments?.getString("note") ?: ""

        // Ако ID-то не е -1, значи редактираме съществуващо пътуване
        if (travelId != -1) {
            editTitle.setText(travelTitle)
            editNote.setText(travelNote)
            buttonSave.text = "Обнови пътуването"
        }

        buttonSave.setOnClickListener {
            val titleText = editTitle.text.toString()
            val noteText = editNote.text.toString()

            if (titleText.isNotBlank()) {
                if (travelId == -1) {
                    // Ако е ново, използваме старата функция insert
                    viewModel.insert(titleText, noteText)
                } else {
                    // Ако редактираме, създаваме обновения обект и го пращаме към update
                    val updatedTravel = Travel(id = travelId, title = titleText, note = noteText, imagePath = null)
                    viewModel.update(updatedTravel)
                }

                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Моля, въведете заглавие!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}