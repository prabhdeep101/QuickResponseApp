package com.example.quickresponseapp

import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.todolist.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import java.util.*

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 🟢 Set up the language spinner
        val langSelect = view.findViewById<Spinner>(R.id.lang_select)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            listOf("English", "Te Reo Māori")
        )
        langSelect.adapter = adapter

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val savedLang = prefs.getString("app_lang", "en")
        langSelect.setSelection(if (savedLang == "mi") 1 else 0)

        langSelect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLang = if (position == 0) "en" else "mi"
                if (selectedLang != savedLang) {
                    setLocale(selectedLang)
                    prefs.edit().putString("app_lang", selectedLang).apply()
                    requireActivity().recreate()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        //pop up menu stuff
        val drawerLayout = view.findViewById<DrawerLayout>(R.id.drawer_layout)
        val menuButton = view.findViewById<MaterialButton>(R.id.menu_button)
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(androidx.core.view.GravityCompat.END)
        }

        //back button stuff
        val backButton = view.findViewById<TextView>(R.id.back_button)
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }

    //language stuff
    private fun setLocale(langCode: String) {
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)
    }
}