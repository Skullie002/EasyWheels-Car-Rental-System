package com.example.easywheels

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailInput = findViewById<TextInputEditText>(R.id.emailInput)
        val passwordInput = findViewById<TextInputEditText>(R.id.passwordInput)
        val loginButton = findViewById<MaterialButton>(R.id.loginButton)
        val registerLink = findViewById<TextView>(R.id.registerLink) // Fixed ID

        val dbHelper = DatabaseHelper(this)

        // 🔹 Go to Register
        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // 🔹 Login Button
        loginButton.setOnClickListener {

            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
            // 🔥 ADMIN LOGIN
            else if (email == "admin@gmail.com" && password == "admin") {
                Toast.makeText(this, "Admin Login", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
                finish()
            }
            // 👤 NORMAL USER LOGIN
            else if (dbHelper.checkUser(email, password)) {
                // ✅ SAVE SESSION
                val sharedPref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putBoolean("isLoggedIn", true)
                editor.putString("userEmail", email)
                editor.apply()

                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                // 🔥 HANDLE FLOW
                val fromBooking = intent.getBooleanExtra("FROM_BOOKING", false)
                val carName = intent.getStringExtra("CAR_NAME")

                if (fromBooking) {
                    val bookingIntent = Intent(this, BookingActivity::class.java)
                    bookingIntent.putExtra("CAR_NAME", carName)
                    startActivity(bookingIntent)
                    finish()
                } else {
                    finish()
                }
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}