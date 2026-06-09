package com.example.mobileapps2025_2301681053.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mobileapps2025_2301681053.R
import com.example.mobileapps2025_2301681053.model.Travel
import com.example.mobileapps2025_2301681053.model.TravelDatabase
import com.example.mobileapps2025_2301681053.repository.TravelRepository
import com.example.mobileapps2025_2301681053.viewmodel.TravelViewModel
import com.example.mobileapps2025_2301681053.viewmodel.TravelViewModelFactory
import java.io.File

class AddEditFragment : Fragment(R.layout.fragment_add_edit) {

    private lateinit var viewModel: TravelViewModel

    // Тук ще пазим пътя до снимката, за да го запишем в базата данни
    private var currentImagePath: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = TravelDatabase.getDatabase(requireContext())
        val repository = TravelRepository(database.travelDao())
        val factory = TravelViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TravelViewModel::class.java]

        val editTitle = view.findViewById<EditText>(R.id.editTextTitle)
        val editNote = view.findViewById<EditText>(R.id.editTextNote)
        val imageViewPhoto = view.findViewById<ImageView>(R.id.imageViewPhoto)
        val buttonCamera = view.findViewById<Button>(R.id.buttonCamera)
        val buttonSave = view.findViewById<Button>(R.id.buttonSave)

        // --- ЛОГИКА ЗА КАМЕРАТА ---
        // Този "лаунчър" се грижи за стартирането на камерата и взимането на резултата
        val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            if (bitmap != null) {
                // 1. Показваме снимката на екрана
                imageViewPhoto.setImageBitmap(bitmap)
                // 2. Запазваме я във файл и пазим пътя до нея
                currentImagePath = saveImageToInternalStorage(bitmap)
            }
        }

        // Когато цъкнем бутона "Снимай", стартираме лаунчъра
        buttonCamera.setOnClickListener {
            takePictureLauncher.launch(null)
        }
        // ---------------------------

        val travelId = arguments?.getInt("id", -1) ?: -1
        val travelTitle = arguments?.getString("title") ?: ""
        val travelNote = arguments?.getString("note") ?: ""
        val travelImagePath = arguments?.getString("imagePath")

        // Ако редактираме съществуващо пътуване
        if (travelId != -1) {
            editTitle.setText(travelTitle)
            editNote.setText(travelNote)
            buttonSave.text = "Обнови пътуването"

            // Ако пътуването има запазена снимка, я зареждаме от файла
            if (travelImagePath != null) {
                currentImagePath = travelImagePath
                val bitmap = BitmapFactory.decodeFile(travelImagePath)
                imageViewPhoto.setImageBitmap(bitmap)
            }
        }

        buttonSave.setOnClickListener {
            val titleText = editTitle.text.toString()
            val noteText = editNote.text.toString()

            if (titleText.isNotBlank()) {
                if (travelId == -1) {
                    // Изпращаме и пътя до снимката
                    viewModel.insert(titleText, noteText, currentImagePath)
                } else {
                    // Обновяваме с пътя до снимката
                    val updatedTravel = Travel(id = travelId, title = titleText, note = noteText, imagePath = currentImagePath)
                    viewModel.update(updatedTravel)
                }
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Моля, въведете заглавие!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Помощна функция: Взима Bitmap (снимката) и я запазва като реален .jpg файл в телефона
    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val filename = "travel_image_${System.currentTimeMillis()}.jpg"
        // Създаваме файл във вътрешната директория на приложението (не изисква permissions)
        val file = File(requireContext().filesDir, filename)
        file.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return file.absolutePath // Връщаме пълния път (напр. /data/user/0/com.example.../travel_image_123.jpg)
    }
}