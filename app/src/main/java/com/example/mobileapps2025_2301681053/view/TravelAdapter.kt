package com.example.mobileapps2025_2301681053.view

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapps2025_2301681053.R
import com.example.mobileapps2025_2301681053.model.Travel

class TravelAdapter(private val onItemClicked: (Travel) -> Unit) : RecyclerView.Adapter<TravelAdapter.TravelViewHolder>() {

    private var travels = emptyList<Travel>()

    class TravelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.textTitle)
        val noteText: TextView = itemView.findViewById(R.id.textNote)
        val imageThumbnail: ImageView = itemView.findViewById(R.id.imageThumbnail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_travel, parent, false)
        return TravelViewHolder(view)
    }

    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        val currentTravel = travels[position]
        holder.titleText.text = currentTravel.title
        holder.noteText.text = currentTravel.note

        if (currentTravel.imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(currentTravel.imagePath)
            holder.imageThumbnail.setImageBitmap(bitmap)
        } else {
            holder.imageThumbnail.setImageBitmap(null)
            holder.imageThumbnail.setBackgroundColor(android.graphics.Color.LTGRAY)
        }

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