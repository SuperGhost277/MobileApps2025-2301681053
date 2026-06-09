package com.example.mobileapps2025_2301681053.view

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mobileapps2025_2301681053.R
import com.example.mobileapps2025_2301681053.model.Travel
import com.example.mobileapps2025_2301681053.model.TravelDatabase
import com.example.mobileapps2025_2301681053.repository.TravelRepository
import com.example.mobileapps2025_2301681053.viewmodel.TravelViewModel
import com.example.mobileapps2025_2301681053.viewmodel.TravelViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File

class AddEditFragment : Fragment(R.layout.fragment_add_edit) {

    private lateinit var viewModel: TravelViewModel
    private var currentImagePath: String? = null

    // НОВО: Променливи за GPS
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double? = null
    private var longitude: Double? = null
    private lateinit var textViewLocation: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = TravelDatabase.getDatabase(requireContext())
        val repository = TravelRepository(database.travelDao())
        val factory = TravelViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TravelViewModel::class.java]

        // Инициализираме услугата за локация на Google
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val editTitle = view.findViewById<EditText>(R.id.editTextTitle)
        val editNote = view.findViewById<EditText>(R.id.editTextNote)
        val imageViewPhoto = view.findViewById<ImageView>(R.id.imageViewPhoto)
        val buttonCamera = view.findViewById<Button>(R.id.buttonCamera)
        val buttonLocation = view.findViewById<Button>(R.id.buttonLocation)
        textViewLocation = view.findViewById(R.id.textViewLocation)
        val buttonSave = view.findViewById<Button>(R.id.buttonSave)

        // --- ЛОГИКА ЗА КАМЕРАТА ---
        val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            if (bitmap != null) {
                imageViewPhoto.setImageBitmap(bitmap)
                currentImagePath = saveImageToInternalStorage(bitmap)
            }
        }
        buttonCamera.setOnClickListener { takePictureLauncher.launch(null) }

        // --- ЛОГИКА ЗА GPS ЛОКАЦИЯ ---
        // Лаунчър, който пита потребителя: "Даваш ли достъп до GPS?"
        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getLastLocation()
            } else {
                Toast.makeText(requireContext(), "Разрешението за локация е отказано!", Toast.LENGTH_SHORT).show()
            }
        }

        buttonLocation.setOnClickListener {
            // Проверяваме дали вече имаме разрешение
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            } else {
                // Ако нямаме, го искаме от потребителя
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        // --- ЗАРЕЖДАНЕ ПРИ РЕДАКЦИЯ ---
        val travelId = arguments?.getInt("id", -1) ?: -1
        val travelTitle = arguments?.getString("title") ?: ""
        val travelNote = arguments?.getString("note") ?: ""
        val travelImagePath = arguments?.getString("imagePath")
        val travelLat = arguments?.getDouble("latitude", Double.NaN).takeUnless { it?.isNaN() == true }
        val travelLon = arguments?.getDouble("longitude", Double.NaN).takeUnless { it?.isNaN() == true }

        if (travelId != -1) {
            editTitle.setText(travelTitle)
            editNote.setText(travelNote)
            buttonSave.text = "Обнови пътуването"

            if (travelImagePath != null) {
                currentImagePath = travelImagePath
                val bitmap = BitmapFactory.decodeFile(travelImagePath)
                imageViewPhoto.setImageBitmap(bitmap)
            }

            // Възстановяваме координатите, ако съществуват
            if (travelLat != null && travelLon != null) {
                latitude = travelLat
                longitude = travelLon
                textViewLocation.text = "Локация: Лат: %.4f, Лон: %.4f".format(latitude, longitude)
            }
        }

        // --- ЗАПАЗВАНЕ ---
        buttonSave.setOnClickListener {
            val titleText = editTitle.text.toString()
            val noteText = editNote.text.toString()

            if (titleText.isNotBlank()) {
                if (travelId == -1) {
                    // Подаваме заглавие, бележка, снимка и GPS координати
                    viewModel.insert(titleText, noteText, currentImagePath, latitude, longitude)
                } else {
                    val updatedTravel = Travel(id = travelId, title = titleText, note = noteText, imagePath = currentImagePath, latitude = latitude, longitude = longitude)
                    viewModel.update(updatedTravel)
                }
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Моля, въведете заглавие!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Функция, която реално взима координатите от GPS чипа
    private fun getLastLocation() {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    textViewLocation.text = "Локация: Лат: %.4f, Лон: %.4f".format(latitude, longitude)
                    Toast.makeText(requireContext(), "Локацията е успешно взета!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Включете GPS-а на телефона!", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val filename = "travel_image_${System.currentTimeMillis()}.jpg"
        val file = File(requireContext().filesDir, filename)
        file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
        return file.absolutePath
    }
}