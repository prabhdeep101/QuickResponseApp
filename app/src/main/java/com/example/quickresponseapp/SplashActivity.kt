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

        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                val hasSeenDisclaimer = AppPreferences.hasSeenDisclaimer(this@SplashActivity).first()

                val intent = Intent(this@SplashActivity, MainActivity::class.java).apply {
                    putExtra("show_disclaimer", !hasSeenDisclaimer)
                }

                startActivity(intent)
                finish()
            }
        }, 1500)
    }
}
