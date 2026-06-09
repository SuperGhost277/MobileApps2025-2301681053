package com.example.mobileapps2025_2301681053.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mobileapps2025_2301681053.R
import com.example.mobileapps2025_2301681053.model.TravelDatabase
import com.example.mobileapps2025_2301681053.repository.TravelRepository
import com.example.mobileapps2025_2301681053.viewmodel.TravelViewModel
import com.example.mobileapps2025_2301681053.viewmodel.TravelViewModelFactory

class AddEditFragment : Fragment() {

    private val viewModel: TravelViewModel by viewModels {
        val database = TravelDatabase.getDatabase(requireContext())
        val repository = TravelRepository(database.travelDao())
        TravelViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTextTitle = view.findViewById<EditText>(R.id.editTextTitle)
        val editTextNote = view.findViewById<EditText>(R.id.editTextNote)
        val buttonSave = view.findViewById<Button>(R.id.buttonSave)

        buttonSave.setOnClickListener {
            val title = editTextTitle.text.toString()
            val note = editTextNote.text.toString()

            if (title.isNotBlank()) {
                viewModel.insert(title, note)
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Моля въведете заглавие", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
