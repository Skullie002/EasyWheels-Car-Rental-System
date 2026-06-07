package com.example.easywheels

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*

class BookingActivity : AppCompatActivity() {

    private var startTimestamp: Long = 0
    private var endTimestamp: Long = 0
    private var startDateStr = ""
    private var endDateStr = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        val carName = intent.getStringExtra("CAR_NAME")
        val carPrice = intent.getStringExtra("CAR_PRICE")

        val carNameText = findViewById<TextView>(R.id.carNameText)
        val startBtn = findViewById<MaterialButton>(R.id.startDateBtn)
        val endBtn = findViewById<MaterialButton>(R.id.endDateBtn)
        val confirmBtn = findViewById<MaterialButton>(R.id.confirmBookingBtn)
        val backBtn = findViewById<ImageButton>(R.id.backBtn)

        backBtn.setOnClickListener { finish() }
        carNameText.text = carName

        startBtn.setOnClickListener {
            showDatePicker { date, ts ->
                startDateStr = date
                startTimestamp = ts
                startBtn.text = date
            }
        }

        endBtn.setOnClickListener {
            showDatePicker { date, ts ->
                if (ts <= startTimestamp) {
                    Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show()
                } else {
                    endDateStr = date
                    endTimestamp = ts
                    endBtn.text = date
                }
            }
        }

        confirmBtn.setOnClickListener {
            if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
                Toast.makeText(this, "Please select dates", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val diff = endTimestamp - startTimestamp
            val days = (diff / (1000 * 60 * 60 * 24)).toInt()
            val totalDays = if (days == 0) 1 else days

            val intent = Intent(this, BookingSummaryActivity::class.java)
            intent.putExtra("CAR_NAME", carName)
            intent.putExtra("CAR_PRICE", carPrice)
            intent.putExtra("START_DATE", startDateStr)
            intent.putExtra("END_DATE", endDateStr)
            intent.putExtra("TOTAL_DAYS", totalDays)
            startActivity(intent)
        }
    }

    private fun showDatePicker(onDateSelected: (String, Long) -> Unit) {
        val cal = Calendar.getInstance()
        val dialog = DatePickerDialog(
            this,
            { _, year, month, day ->
                val selectedCal = Calendar.getInstance()
                selectedCal.set(year, month, day)
                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                onDateSelected(sdf.format(selectedCal.time), selectedCal.timeInMillis)
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        dialog.datePicker.minDate = System.currentTimeMillis() - 1000
        dialog.show()
    }
}