package com.example.quickresponseapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.R
import com.example.todolist.R.*

class EmergencyCallActivity : AppCompatActivity() {

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_call)

        // Delay for 2 seconds then move to DisclaimerActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, HomeScreen::class.java))
            finish() // Close splash screen
        }, 10000)

        val endButton: ImageButton = findViewById(id.endButton)
        endButton.setOnClickListener {
            startActivity(Intent(this, HomeScreen::class.java))
            finish() // Optional: closes this screen


        }
    }
}
