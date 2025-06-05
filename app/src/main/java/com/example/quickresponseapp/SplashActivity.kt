package com.example.quickresponseapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay for 2 seconds then move to DisclaimerActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, DisclaimerActivity::class.java))
            finish() // Close splash screen
        }, 2000)
    }
}
