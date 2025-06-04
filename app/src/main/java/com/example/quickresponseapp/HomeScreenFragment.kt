package com.example.quickresponseapp

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.content.res.Configuration
import android.content.res.Resources
import android.preference.PreferenceManager
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.todolist.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import java.util.*

class HomeScreenFragment : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(newBase)
        val langCode = prefs.getString("app_lang", "en") ?: "en"

        val locale = Locale(langCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        val context = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            newBase?.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            newBase?.resources?.updateConfiguration(config, newBase.resources.displayMetrics)
            newBase
        }

        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)

        // Pop-out menu setup
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val menuButton = findViewById<MaterialButton>(R.id.menu_button)

        val drawerWidth = (Resources.getSystem().displayMetrics.widthPixels * 0.75).toInt()
        navView.layoutParams.width = drawerWidth
        navView.requestLayout()

        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END)
        }

        //settings button
        val settingsButton = findViewById<ImageButton>(R.id.settings_button)
        settingsButton.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
