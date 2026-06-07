package com.example.easywheels

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class CarDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details)

        val carName = intent.getStringExtra("CAR_NAME")
        val carPrice = intent.getStringExtra("CAR_PRICE")
        val carImage = intent.getIntExtra("CAR_IMAGE", 0)

        val nameText = findViewById<TextView>(R.id.carNameDetail)
        val priceText = findViewById<TextView>(R.id.carPriceDetail)
        val imageView = findViewById<ImageView>(R.id.carImageDetail)
        val bookButton = findViewById<MaterialButton>(R.id.bookButton)
        val backBtn = findViewById<ImageButton>(R.id.backBtn)

        nameText.text = carName
        priceText.text = carPrice
        if (carImage != 0) {
            imageView.setImageResource(carImage)
        }

        backBtn.setOnClickListener { finish() }

        bookButton.setOnClickListener {
            val sharedPref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
            val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

            if (isLoggedIn) {
                val intent = Intent(this, BookingActivity::class.java)
                intent.putExtra("CAR_NAME", carName)
                intent.putExtra("CAR_PRICE", carPrice)
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("FROM_BOOKING", true)
                intent.putExtra("CAR_NAME", carName)
                intent.putExtra("CAR_PRICE", carPrice)
                startActivity(intent)
            }
        }
    }
}