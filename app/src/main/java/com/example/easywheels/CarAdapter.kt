package com.example.easywheels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class CarAdapter(
    private val carList: List<Car>,
    private val dbHelper: DatabaseHelper
) : RecyclerView.Adapter<CarAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.carName)
        val details: TextView = view.findViewById(R.id.carDetails)
        val status: TextView = view.findViewById(R.id.carStatus)
        val toggleBtn: Button = view.findViewById(R.id.toggleBtn)
        val deleteBtn: Button = view.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_car, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val car = carList[position]

        holder.name.text = car.name
        holder.details.text = "${car.price} | ${car.location}"
        holder.status.text = if (car.available == 1) "Available" else "Unavailable"

        holder.toggleBtn.setOnClickListener {
            val newStatus = if (car.available == 1) 0 else 1
            dbHelper.updateCarAvailability(car.id, newStatus)
            holder.status.text = if (newStatus == 1) "Available" else "Unavailable"
        }

        holder.deleteBtn.setOnClickListener {
            dbHelper.deleteCar(car.id)
            Toast.makeText(holder.itemView.context, "Car Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = carList.size
}