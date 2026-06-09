package com.example.mobileapps2025_2301681053.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapps2025_2301681053.R
import com.example.mobileapps2025_2301681053.model.TravelDatabase
import com.example.mobileapps2025_2301681053.repository.TravelRepository
import com.example.mobileapps2025_2301681053.viewmodel.TravelViewModel
import com.example.mobileapps2025_2301681053.viewmodel.TravelViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class ListFragment : Fragment(R.layout.fragment_list) {

    private lateinit var viewModel: TravelViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Подготовка на Базата данни, Репозиторито и ViewModel-а
        val database = TravelDatabase.getDatabase(requireContext())
        val repository = TravelRepository(database.travelDao())
        val factory = TravelViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TravelViewModel::class.java]

        // 2. Настройка на списъка (RecyclerView) и Адаптера
        val adapter = TravelAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        // Казваме на списъка да подрежда елементите вертикално (един под друг)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 3. Наблюдаване на базата данни за промени
        // Когато се добави ново пътуване, адаптерът автоматично ще получи новите данни!
        lifecycleScope.launch {
            viewModel.allTravels.collect { travels ->
                adapter.setData(travels)
            }
        }

        // 4. Логиката за бутона "+" (остава същата)
        val fabAdd = view.findViewById<FloatingActionButton>(R.id.fabAdd)
        fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addEditFragment)
        }
    }
}