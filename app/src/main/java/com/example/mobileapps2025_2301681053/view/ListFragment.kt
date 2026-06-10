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

        val database = TravelDatabase.getDatabase(requireContext())
        val repository = TravelRepository(database.travelDao())
        val factory = TravelViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TravelViewModel::class.java]

        val adapter = TravelAdapter { clickedTravel ->
            val bundle = Bundle().apply {
                putInt("id", clickedTravel.id)
                putString("title", clickedTravel.title)
                putString("note", clickedTravel.note)
                putString("imagePath", clickedTravel.imagePath)
                clickedTravel.latitude?.let { putDouble("latitude", it) }
                clickedTravel.longitude?.let { putDouble("longitude", it) }
            }
            findNavController().navigate(R.id.action_listFragment_to_addEditFragment, bundle)
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val swipeToDeleteCallback = object : androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(0, androidx.recyclerview.widget.ItemTouchHelper.LEFT or androidx.recyclerview.widget.ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val travelToDelete = adapter.getTravelAt(position)
                viewModel.delete(travelToDelete)

                android.widget.Toast.makeText(requireContext(), "Journey deleted!", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        val itemTouchHelper = androidx.recyclerview.widget.ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        lifecycleScope.launch {
            viewModel.allTravels.collect { travels ->
                adapter.setData(travels)
            }
        }

        val fabAdd = view.findViewById<FloatingActionButton>(R.id.fabAdd)
        fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addEditFragment)
        }
    }
}