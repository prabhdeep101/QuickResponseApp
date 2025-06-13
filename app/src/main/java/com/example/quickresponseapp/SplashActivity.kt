package com.example.quickresponseapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Delay execution to show splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            // check if disclaimer has been seen
            lifecycleScope.launch {
                val hasSeenDisclaimer = AppPreferences.hasSeenDisclaimer(this@SplashActivity).first()
                // Create intent to go to main activity
                val intent = Intent(this@SplashActivity, MainActivity::class.java).apply {
                    putExtra("show_disclaimer", !hasSeenDisclaimer)
                }

                startActivity(intent)
                finish()
            }
        }, 1500)
    }
}
