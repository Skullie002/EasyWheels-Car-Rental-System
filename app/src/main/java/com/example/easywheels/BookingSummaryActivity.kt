package com.example.easywheels

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class BookingSummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_summary)

        val carName = intent.getStringExtra("CAR_NAME") ?: "Unknown Car"
        val carPrice = intent.getStringExtra("CAR_PRICE") ?: "₹0/day"
        val startDate = intent.getStringExtra("START_DATE") ?: ""
        val endDate = intent.getStringExtra("END_DATE") ?: ""
        val totalDays = intent.getIntExtra("TOTAL_DAYS", 1)

        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        val carNameText = findViewById<TextView>(R.id.summaryCarName)
        val startDateText = findViewById<TextView>(R.id.summaryStartDate)
        val endDateText = findViewById<TextView>(R.id.summaryEndDate)
        val totalPriceText = findViewById<TextView>(R.id.summaryTotalPrice)
        val paymentBtn = findViewById<MaterialButton>(R.id.proceedPaymentBtn)

        backBtn.setOnClickListener { finish() }

        carNameText.text = carName
        startDateText.text = startDate
        endDateText.text = endDate

        // Calculate Total Price
        val pricePerDay = carPrice.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 0
        val totalAmount = pricePerDay * totalDays
        totalPriceText.text = "₹$totalAmount"

        paymentBtn.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("CAR_NAME", carName)
            intent.putExtra("START_DATE", startDate)
            intent.putExtra("END_DATE", endDate)
            intent.putExtra("TOTAL_AMOUNT", totalAmount)
            startActivity(intent)
        }
    }
}