package com.example.mobileapps2025_2301681053.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mobileapps2025_2301681053.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

// Казваме на фрагмента кой XML дизайн да използва
class ListFragment : Fragment(R.layout.fragment_list) {

    // Тази функция се стартира веднага щом екранът се зареди
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Намираме кръглия бутон по неговото ID
        val fabAdd = view.findViewById<FloatingActionButton>(R.id.fabAdd)

        // Казваме какво да се случи при кликване
        fabAdd.setOnClickListener {
            // Използваме Navigation Component, за да преминем към другия екран
            findNavController().navigate(R.id.action_listFragment_to_addEditFragment)
        }
    }
}
