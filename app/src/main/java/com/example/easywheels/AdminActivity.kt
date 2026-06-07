package com.example.easywheels

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val bookingRecycler = findViewById<RecyclerView>(R.id.adminRecyclerView)
        val carRecycler = findViewById<RecyclerView>(R.id.carRecyclerView)

        val carNameInput = findViewById<EditText>(R.id.carNameInput)
        val carPriceInput = findViewById<EditText>(R.id.carPriceInput)
        val carLocationInput = findViewById<EditText>(R.id.carLocationInput)
        val addCarBtn = findViewById<Button>(R.id.addCarBtn)
        
        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        val logoutBtn = findViewById<MaterialButton>(R.id.adminLogoutBtn)

        val dbHelper = DatabaseHelper(this)

        // 🔹 Back Button
        backBtn.setOnClickListener { finish() }

        // 🔹 Logout Button
        logoutBtn.setOnClickListener {
            val sharedPref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
            sharedPref.edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // 🔥 ADD CAR
        addCarBtn.setOnClickListener {
            val name = carNameInput.text.toString().trim()
            val price = carPriceInput.text.toString().trim()
            val location = carLocationInput.text.toString().trim()

            if (name.isEmpty() || price.isEmpty() || location.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val success = dbHelper.addCar(name, price, "default", location)
                if (success) {
                    Toast.makeText(this, "Car Added", Toast.LENGTH_SHORT).show()
                    recreate()
                } else {
                    Toast.makeText(this, "Error adding car", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 🔥 LOAD BOOKINGS
        loadBookings(dbHelper, bookingRecycler)

        // 🔥 LOAD CARS
        loadCars(dbHelper, carRecycler)
    }

    private fun loadBookings(dbHelper: DatabaseHelper, recyclerView: RecyclerView) {
        val bookingCursor: Cursor = dbHelper.getAllBookings()
        val bookingList = mutableListOf<Booking>()
        while (bookingCursor.moveToNext()) {
            val id = bookingCursor.getInt(bookingCursor.getColumnIndexOrThrow("id"))
            val user = bookingCursor.getString(bookingCursor.getColumnIndexOrThrow("user_email"))
            val car = bookingCursor.getString(bookingCursor.getColumnIndexOrThrow("car_name"))
            val start = bookingCursor.getString(bookingCursor.getColumnIndexOrThrow("start_date"))
            val end = bookingCursor.getString(bookingCursor.getColumnIndexOrThrow("end_date"))
            val status = bookingCursor.getString(bookingCursor.getColumnIndexOrThrow("status"))
            bookingList.add(Booking(id, user, car, start, end, status))
        }
        bookingCursor.close()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AdminAdapter(bookingList, dbHelper)
    }

    private fun loadCars(dbHelper: DatabaseHelper, recyclerView: RecyclerView) {
        val carCursor = dbHelper.getAllCars()
        val carList = mutableListOf<Car>()
        while (carCursor.moveToNext()) {
            val id = carCursor.getInt(carCursor.getColumnIndexOrThrow("id"))
            val name = carCursor.getString(carCursor.getColumnIndexOrThrow("name"))
            val price = carCursor.getString(carCursor.getColumnIndexOrThrow("price"))
            val location = carCursor.getString(carCursor.getColumnIndexOrThrow("location"))
            val available = carCursor.getInt(carCursor.getColumnIndexOrThrow("available"))
            carList.add(Car(id, name, price, location, available))
        }
        carCursor.close()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CarAdapter(carList, dbHelper)
    }
}