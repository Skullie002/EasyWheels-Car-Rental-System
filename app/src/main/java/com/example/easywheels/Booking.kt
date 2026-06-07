package com.example.easywheels

data class Booking(
    val id: Int,
    val user: String,
    val car: String,
    val startDate: String,
    val endDate: String,
    val status: String
)