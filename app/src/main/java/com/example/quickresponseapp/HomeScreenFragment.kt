package com.example.quickresponseapp

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.content.res.Configuration
import android.content.res.Resources
import android.preference.PreferenceManager
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.todolist.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import java.util.*

class HomeScreenFragment : Fragment(R.layout.fragment_home) {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val langCode = prefs.getString("app_lang", "en") ?: "en"

        val locale = Locale(langCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val drawerLayout = activity?.findViewById<DrawerLayout>(R.id.drawer_layout)
        val navView = activity?.findViewById<NavigationView>(R.id.nav_view)
        val menuButton = view.findViewById<MaterialButton>(R.id.menu_button)

        val drawerWidth = (Resources.getSystem().displayMetrics.widthPixels * 0.75).toInt()
        navView?.layoutParams?.width = drawerWidth
        navView?.requestLayout()

        menuButton.setOnClickListener {
            drawerLayout?.openDrawer(GravityCompat.END)
        }

        val settingsButton = view.findViewById<ImageButton>(R.id.settings_button)
        settingsButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.settings_button, SettingsFragment()) 
                .addToBackStack(null)
                .commit()
        }
    }

}
