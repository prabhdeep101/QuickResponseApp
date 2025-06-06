package com.example.quickresponseapp

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onAttach(context: Context) {
        super.onAttach(LangHelper.applySavedLocale(context))
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

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val savedLang = prefs.getString("app_lang", "en")
        langSelect.setSelection(if (savedLang == "mi") 1 else 0)

        langSelect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, v: View?, position: Int, id: Long) {
                val selectedLang = if (position == 0) "en" else "mi"
                if (selectedLang != savedLang) {
                    prefs.edit().putString("app_lang", selectedLang).apply()
                    LangHelper.updateLocale(requireContext(), selectedLang)
                    requireActivity().recreate()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val editContactsButton = view.findViewById<MaterialButton>(R.id.edit_contact_btn)
        editContactsButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_contactsFragment)
        }

        view.findViewById<TextView>(R.id.back_button).setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
