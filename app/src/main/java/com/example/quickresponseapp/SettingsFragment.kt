package com.example.quickresponseapp

import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import java.util.*

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Language dropdown setup
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
            override fun onItemSelected(parent: AdapterView<*>?, v: View?, position: Int, id: Long) {
                val selectedLang = if (position == 0) "en" else "mi"
                if (selectedLang != savedLang) {
                    setLocale(selectedLang)
                    prefs.edit().putString("app_lang", selectedLang).apply()
                    requireActivity().recreate()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        view.findViewById<TextView>(R.id.back_button).setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setLocale(langCode: String) {
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)
    }
}
