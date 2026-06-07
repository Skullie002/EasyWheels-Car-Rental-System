package com.example.easywheels

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val transactionInput = findViewById<TextInputEditText>(R.id.transactionInput)
        val submitBtn = findViewById<MaterialButton>(R.id.submitPaymentBtn)
        val totalText = findViewById<TextView>(R.id.paymentTotalText)
        val backBtn = findViewById<ImageButton>(R.id.backBtn)

        backBtn.setOnClickListener { finish() }

        // Get data from previous screen
        val carName = intent.getStringExtra("CAR_NAME") ?: "Car"
        val startDate = intent.getStringExtra("START_DATE") ?: ""
        val endDate = intent.getStringExtra("END_DATE") ?: ""
        val totalAmount = intent.getIntExtra("TOTAL_AMOUNT", 0)

        totalText.text = "Total: ₹$totalAmount"

        // Get logged-in user
        val sharedPref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
        val userEmail = sharedPref.getString("userEmail", "")

        val dbHelper = DatabaseHelper(this)

        submitBtn.setOnClickListener {
            val txnId = transactionInput.text.toString().trim()

            if (txnId.length < 10) {
                Toast.makeText(this, "Please enter a valid Transaction ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = dbHelper.insertBooking(
                userEmail ?: "Guest",
                carName,
                "Kochi", // Ideally pass location here too
                startDate,
                endDate,
                txnId
            )

            if (success) {
                Toast.makeText(this, "Payment Verified! Booking Confirmed.", Toast.LENGTH_LONG).show()
                
                // Redirect to Profile
                val intent = Intent(this, ProfileActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Database error. Try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}