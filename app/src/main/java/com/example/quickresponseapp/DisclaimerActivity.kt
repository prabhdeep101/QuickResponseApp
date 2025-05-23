package com.example.quickresponseapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.R

class DisclaimerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disclaimer)

        val acceptButton: Button = findViewById(R.id.acceptButton)
        acceptButton.setOnClickListener {
            startActivity(Intent(this, HomeScreen::class.java))
            finish() // Optional: closes this screen
        }
    }
}
