package com.example.easywheels

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class VehicleListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_list)

        val location = intent.getStringExtra("LOCATION") ?: "Kochi"
        findViewById<TextView>(R.id.locationTitle).text = "Cars in $location"

        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        val profileIcon = findViewById<ShapeableImageView>(R.id.profileIcon)
        val recyclerView = findViewById<RecyclerView>(R.id.vehicleRecyclerView)

        backBtn.setOnClickListener { finish() }
        
        profileIcon.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbHelper = DatabaseHelper(this)
        
        if (dbHelper.isCarsTableEmpty()) {
            dbHelper.insertSampleCars()
        }

        val cursor: Cursor = dbHelper.getCarsByLocation(location)
        val vehicleList = mutableListOf<Vehicle>()

        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val price = cursor.getString(cursor.getColumnIndexOrThrow("price"))
            val imageStr = cursor.getString(cursor.getColumnIndexOrThrow("image"))
            
            // 🔥 Dynamically get local drawable resource ID from the name stored in DB
            val imageRes = resources.getIdentifier(imageStr, "drawable", packageName)
            
            // Fallback if image not found
            val finalImageRes = if (imageRes != 0) imageRes else android.R.drawable.ic_menu_gallery
            
            vehicleList.add(Vehicle(name, price, finalImageRes))
        }
        cursor.close()

        recyclerView.adapter = VehicleAdapter(vehicleList) { vehicle ->
            val intent = Intent(this, CarDetailsActivity::class.java)
            intent.putExtra("CAR_NAME", vehicle.name)
            intent.putExtra("CAR_PRICE", vehicle.price)
            intent.putExtra("CAR_IMAGE", vehicle.image)
            startActivity(intent)
        }
    }
}