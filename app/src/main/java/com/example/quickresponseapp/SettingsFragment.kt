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

    private var savedLang: String = "en"

    override fun onAttach(context: Context) {
        super.onAttach(AppPreferences.applySavedLocale(context))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val langSelect = view.findViewById<Spinner>(R.id.lang_select)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            listOf("English", "Te Reo Māori")
        )
        langSelect.adapter = adapter

        lifecycleScope.launch {
            savedLang = AppPreferences.getLanguage(requireContext())
            langSelect.setSelection(if (savedLang == "mi") 1 else 0)
        }

        langSelect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, v: View?, position: Int, id: Long) {
                val selectedLang = if (position == 0) "en" else "mi"
                if (selectedLang != savedLang) {
                    lifecycleScope.launch {
                        AppPreferences.setLanguage(requireContext(), selectedLang)
                        requireActivity().recreate()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        view.findViewById<Button>(R.id.edit_profile_btn).setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_editProfileFragment)
        }

        view.findViewById<MaterialButton>(R.id.edit_contact_btn).setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_contactsFragment)
        }

        view.findViewById<TextView>(R.id.back_button).setOnClickListener {
            findNavController().navigate(R.id.homeScreenFragment)
        }

        view.findViewById<ImageButton>(R.id.emergency_button).setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_emergencyPageFragment)
        }
    }
}
