package com.example.mobileapps2025_2301681053.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapps2025_2301681053.R
import com.example.mobileapps2025_2301681053.model.Travel

class TravelAdapter : RecyclerView.Adapter<TravelAdapter.TravelViewHolder>() {

    // Тук ще пазим списъка с пътувания
    private var travels = emptyList<Travel>()

    // Този клас държи връзката към елементите в item_travel.xml
    class TravelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.textTitle)
        val noteText: TextView = itemView.findViewById(R.id.textNote)
    }

    // Създава "картичката" за всеки нов ред
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_travel, parent, false)
        return TravelViewHolder(view)
    }

    // Попълва данните (заглавие и бележка) в картичката
    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        val currentTravel = travels[position]
        holder.titleText.text = currentTravel.title
        holder.noteText.text = currentTravel.note
    }

    // Казва на списъка колко общо елемента има
    override fun getItemCount(): Int {
        return travels.size
    }

    // Функция, чрез която ще опресняваме данните, когато се добави ново пътуване
    @SuppressLint("NotifyDataSetChanged")
    fun setData(newTravels: List<Travel>) {
        this.travels = newTravels
        notifyDataSetChanged() // Казваме на списъка да се пренарисува
    }
}