package com.example.easywheels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class AdminAdapter(
    private val bookingList: List<Booking>,
    private val dbHelper: DatabaseHelper
) : RecyclerView.Adapter<AdminAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val user: TextView = view.findViewById(R.id.adminUser)
        val car: TextView = view.findViewById(R.id.adminCar)
        val dates: TextView = view.findViewById(R.id.adminDates)
        val status: TextView = view.findViewById(R.id.adminStatus)
        val approveBtn: Button = view.findViewById(R.id.approveBtn)
        val rejectBtn: Button = view.findViewById(R.id.rejectBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val booking = bookingList[position]  // ✅ ONLY ONCE

        // 🔹 Set data
        holder.user.text = "User: ${booking.user}"
        holder.car.text = "Car: ${booking.car}"
        holder.dates.text = "${booking.startDate} → ${booking.endDate}"
        holder.status.text = "Status: ${booking.status}"

        // 🔹 Approve
        holder.approveBtn.setOnClickListener {
            dbHelper.updateBookingStatus(booking.id, "Approved")
            holder.status.text = "Status: Approved"
        }

        // 🔹 Reject
        holder.rejectBtn.setOnClickListener {
            dbHelper.updateBookingStatus(booking.id, "Rejected")
            holder.status.text = "Status: Rejected"
        }
    }

    override fun getItemCount(): Int = bookingList.size
}