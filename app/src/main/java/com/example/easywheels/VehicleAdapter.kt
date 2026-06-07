package com.example.easywheels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VehicleAdapter(
    private val vehicleList: List<Vehicle>,
    private val onItemClick: (Vehicle) -> Unit
) : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

    class VehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val carImage: ImageView = itemView.findViewById(R.id.carImage)
        val carName: TextView = itemView.findViewById(R.id.carName)
        val carPrice: TextView = itemView.findViewById(R.id.carPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vehicle_card, parent, false)
        return VehicleViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {

        val vehicle = vehicleList[position]

        holder.carImage.setImageResource(vehicle.image)
        holder.carName.text = vehicle.name
        holder.carPrice.text = vehicle.price

        holder.itemView.setOnClickListener {
            onItemClick(vehicle)
        }
    }

    override fun getItemCount(): Int {
        return vehicleList.size
    }
}