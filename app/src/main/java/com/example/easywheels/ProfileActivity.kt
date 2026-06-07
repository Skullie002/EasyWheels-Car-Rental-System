package com.example.easywheels

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val userNameText = findViewById<TextView>(R.id.userNameText)
        val userEmailText = findViewById<TextView>(R.id.userEmailText)
        val phoneText = findViewById<TextView>(R.id.phoneText)
        val bookingText = findViewById<TextView>(R.id.bookingStatusText)

        val logoutBtn = findViewById<MaterialButton>(R.id.logoutBtn)
        val browseBtn = findViewById<MaterialButton>(R.id.browseCarsButton)
        val loginBtn = findViewById<MaterialButton>(R.id.loginButton) // 🔥 NEW

        val sharedPref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
        val email = sharedPref.getString("userEmail", null)

        val dbHelper = DatabaseHelper(this)

        if (email.isNullOrEmpty()) {

            // ❌ NOT LOGGED IN STATE
            userNameText.text = "No user logged in"
            userEmailText.text = ""
            phoneText.text = ""
            bookingText.text = "Login to view bookings"

            loginBtn.visibility = View.VISIBLE
            logoutBtn.visibility = View.GONE

        } else {

            // ✅ LOGGED IN STATE
            loginBtn.visibility = View.GONE
            logoutBtn.visibility = View.VISIBLE

            val user = dbHelper.getUser(email)

            if (user != null) {
                userNameText.text = user.name
                userEmailText.text = user.email
                phoneText.text = user.phone
            }

            // 🔥 Load bookings
            val cursor = dbHelper.getUserBookings(email)

            var bookingInfo = ""

            while (cursor.moveToNext()) {
                val car = cursor.getString(cursor.getColumnIndexOrThrow("car_name"))
                val status = cursor.getString(cursor.getColumnIndexOrThrow("status"))

                bookingInfo += "$car - $status\n"
            }

            cursor.close()

            bookingText.text = if (bookingInfo.isEmpty()) {
                "No bookings yet"
            } else {
                bookingInfo
            }
        }

        // 🔹 Login button
        loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // 🔹 Browse
        browseBtn.setOnClickListener {
            startActivity(Intent(this, LocationSelectionActivity::class.java))
        }

        // 🔹 Logout
        logoutBtn.setOnClickListener {
            val editor = sharedPref.edit()
            editor.clear()
            editor.apply()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}