package com.example.mobileapps2025_2301681053.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapps2025_2301681053.R
import com.example.mobileapps2025_2301681053.model.Travel

// Добавихме (private val onItemClicked: (Travel) -> Unit)
class TravelAdapter(private val onItemClicked: (Travel) -> Unit) : RecyclerView.Adapter<TravelAdapter.TravelViewHolder>() {

    private var travels = emptyList<Travel>()

    class TravelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.textTitle)
        val noteText: TextView = itemView.findViewById(R.id.textNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_travel, parent, false)
        return TravelViewHolder(view)
    }

    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        val currentTravel = travels[position]
        holder.titleText.text = currentTravel.title
        holder.noteText.text = currentTravel.note

        // НОВО: Когато някой кликне върху целия ред, извикваме функцията
        holder.itemView.setOnClickListener {
            onItemClicked(currentTravel)
        }
    }

    override fun getItemCount(): Int {
        return travels.size
    }

    fun setData(newTravels: List<Travel>) {
        this.travels = newTravels
        notifyDataSetChanged()
    }

    fun getTravelAt(position: Int): Travel {
        return travels[position]
    }
}