package com.example.quickresponseapp

import android.content.Context
import android.os.Bundle
import android.content.res.Configuration
import android.content.res.Resources
import android.preference.PreferenceManager
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

        // Only nav drawer sizing logic (which is safe here)
        val navView = activity?.findViewById<NavigationView>(R.id.nav_view)
        val drawerWidth = (Resources.getSystem().displayMetrics.widthPixels * 0.75).toInt()
        navView?.layoutParams?.width = drawerWidth
        navView?.requestLayout()

        val settingsButton = view.findViewById<ImageButton>(R.id.settings_button)
        settingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_settingsFragment)
        }

        val contactsButton = view.findViewById<MaterialButton>(R.id.contacts_button)
        contactsButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_contactsFragment)
        }
    }
}

