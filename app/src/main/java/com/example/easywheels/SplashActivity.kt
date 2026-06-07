package com.example.easywheels

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.logoImage)

        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_zoom)
        logo.startAnimation(animation)

        Handler(Looper.getMainLooper()).postDelayed({

            val intent = Intent(this, LocationSelectionActivity::class.java)
            startActivity(intent)
            finish()

        }, 2500)
    }
}