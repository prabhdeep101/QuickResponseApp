package com.example.quickresponseapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DisclaimerActivity : AppCompatActivity() {

    private lateinit var prefs: android.content.SharedPreferences
    private var isEnglish = true

    // Apply locale before views are created
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LangHelper.applySavedLocale(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disclaimer)

        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val savedLang = prefs.getString("app_lang", "en") ?: "en"
        isEnglish = savedLang == "en"

        val btnToggle = findViewById<Button>(R.id.translateButton)
        val textDisclaimer = findViewById<TextView>(R.id.disclaimerText)
        val btnAccept = findViewById<Button>(R.id.acceptButton)

        // Load strings for current language
        textDisclaimer.text = getString(R.string.disclaimer)
        btnAccept.text = getString(R.string.accept)
        btnToggle.text = if (isEnglish) "Māori" else "English"

        // Language switch button
        btnToggle.setOnClickListener {
            isEnglish = !isEnglish
            val newLang = if (isEnglish) "en" else "mi"
            prefs.edit().putString("app_lang", newLang).apply()

            // Restart activity to reflect language change
            val intent = Intent(this, DisclaimerActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
