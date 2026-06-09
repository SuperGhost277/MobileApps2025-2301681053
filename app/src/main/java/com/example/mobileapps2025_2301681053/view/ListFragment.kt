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
        // Променяме създаването на адаптера
        val adapter = TravelAdapter { clickedTravel ->
            // Опаковаме данните (ID, заглавие и бележка) в един "Пакет" (Bundle)
            val bundle = Bundle().apply {
                putInt("id", clickedTravel.id)
                putString("title", clickedTravel.title)
                putString("note", clickedTravel.note)
            }
            // Отиваме на екрана за добавяне/редактиране, но носим пакета с нас
            findNavController().navigate(R.id.action_listFragment_to_addEditFragment, bundle)
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        // Казваме на списъка да подрежда елементите вертикално (един под друг)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // --- МАГИЯТА ЗА ИЗТРИВАНЕ (Swipe to Delete) ---
        val swipeToDeleteCallback = object : androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(0, androidx.recyclerview.widget.ItemTouchHelper.LEFT or androidx.recyclerview.widget.ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false // Не ни трябва преместване нагоре-надолу
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Взимаме позицията на елемента, който е плъзнат
                val position = viewHolder.bindingAdapterPosition
                // Взимаме самото пътуване от адаптера (трябва да добавим функция getTravelAt в него)
                val travelToDelete = adapter.getTravelAt(position)
                // Казваме на ViewModel-а да го изтрие от базата данни
                viewModel.delete(travelToDelete)

                android.widget.Toast.makeText(requireContext(), "Пътуването е изтрито!", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        val itemTouchHelper = androidx.recyclerview.widget.ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        // ----------------------------------------------

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