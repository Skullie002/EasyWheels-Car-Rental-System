package com.example.easywheels

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val nameInput = findViewById<TextInputEditText>(R.id.nameInput)
        val emailInput = findViewById<TextInputEditText>(R.id.emailInput)
        val phoneInput = findViewById<TextInputEditText>(R.id.phoneInput)
        val passwordInput = findViewById<TextInputEditText>(R.id.passwordInput)
        val registerButton = findViewById<MaterialButton>(R.id.registerButton)
        val backBtn = findViewById<ImageButton>(R.id.backBtn)
        val loginLink = findViewById<TextView>(R.id.loginLink)

        val dbHelper = DatabaseHelper(this)

        backBtn.setOnClickListener { finish() }
        loginLink.setOnClickListener { finish() }

        registerButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            } else {
                val success = dbHelper.insertUser(name, email, phone, password)
                if (success) {
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Registration failed. Email might already exist.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}