package com.example.easywheels

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class LocationSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_selection)

        val db = DatabaseHelper(this)

        if (db.isCarsTableEmpty()) {
            db.insertSampleCars()
        }
        val kochiCard = findViewById<CardView>(R.id.kochiCard)
        val trivandrumCard = findViewById<CardView>(R.id.trivandrumCard)
        val calicutCard = findViewById<CardView>(R.id.calicutCard)
        val munnarCard = findViewById<CardView>(R.id.munnarCard)
        val alappuzhaCard = findViewById<CardView>(R.id.alappuzhaCard)
        val wayanadCard = findViewById<CardView>(R.id.wayanadCard)

        val searchBar = findViewById<EditText>(R.id.searchBar)

        kochiCard.setOnClickListener {
            openVehicleList("Kochi")
        }

        trivandrumCard.setOnClickListener {
            openVehicleList("Trivandrum")
        }

        calicutCard.setOnClickListener {
            openVehicleList("Calicut")
        }

        munnarCard.setOnClickListener {
            openVehicleList("Munnar")
        }

        alappuzhaCard.setOnClickListener {
            openVehicleList("Alappuzha")
        }

        wayanadCard.setOnClickListener {
            openVehicleList("Wayanad")
        }

        searchBar.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {

                val text = s.toString().lowercase()

                kochiCard.visibility =
                    if ("kochi".contains(text)) CardView.VISIBLE else CardView.GONE

                trivandrumCard.visibility =
                    if ("trivandrum".contains(text)) CardView.VISIBLE else CardView.GONE

                calicutCard.visibility =
                    if ("calicut".contains(text)) CardView.VISIBLE else CardView.GONE

                munnarCard.visibility =
                    if ("munnar".contains(text)) CardView.VISIBLE else CardView.GONE

                alappuzhaCard.visibility =
                    if ("alappuzha".contains(text)) CardView.VISIBLE else CardView.GONE

                wayanadCard.visibility =
                    if ("wayanad".contains(text)) CardView.VISIBLE else CardView.GONE
            }
        })
    }

    private fun openVehicleList(location: String) {
        val intent = Intent(this, VehicleListActivity::class.java)
        intent.putExtra("LOCATION", location)
        startActivity(intent)
    }
}