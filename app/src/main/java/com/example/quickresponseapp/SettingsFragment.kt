package com.example.quickresponseapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    // Holds current saved language
    private var savedLang: String = "en"

    // Apply saved language
    override fun onAttach(context: Context) {
        super.onAttach(AppPreferences.applySavedLocale(context))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Find language dropdown
        val langSelect = view.findViewById<Spinner>(R.id.lang_select)
        // Create adapter with language options
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            listOf("English", "Te Reo Māori")
        )
        langSelect.adapter = adapter

        // Load saved language and set dropdown selection
        lifecycleScope.launch {
            savedLang = AppPreferences.getLanguage(requireContext())
            langSelect.setSelection(if (savedLang == "mi") 1 else 0)
        }

        // When a new language is selected
        langSelect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, v: View?, position: Int, id: Long) {
                val selectedLang = if (position == 0) "en" else "mi"
                if (selectedLang != savedLang) {
                    // Save new language and restart activity
                    lifecycleScope.launch {
                        AppPreferences.setLanguage(requireContext(), selectedLang)
                        requireActivity().recreate()
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Navigate to edit profile
        view.findViewById<Button>(R.id.edit_profile_btn).setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_editProfileFragment)
        }
        // Navigate to contacts page
        view.findViewById<MaterialButton>(R.id.edit_contact_btn).setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_contactsFragment)
        }
        // Navigate back to home screen
        view.findViewById<TextView>(R.id.back_button).setOnClickListener {
            findNavController().navigate(R.id.homeScreenFragment)
        }
        // Navigate to emergency page
        view.findViewById<ImageButton>(R.id.emergency_button).setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_emergencyPageFragment)
        }
    }
}
