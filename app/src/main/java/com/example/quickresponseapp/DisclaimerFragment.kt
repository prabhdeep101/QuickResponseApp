package com.example.quickresponseapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch

class DisclaimerFragment : Fragment(R.layout.fragment_disclaimer) {

    private var isEnglish = true

    // Apply locale before views are created
    override fun onAttach(context: Context) {
        super.onAttach(AppPreferences.applySavedLocale(context))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnToggle = view.findViewById<Button>(R.id.translateButton)
        val textDisclaimer = view.findViewById<TextView>(R.id.disclaimerText)
        val btnAccept = view.findViewById<Button>(R.id.acceptButton)

        textDisclaimer.text = getString(R.string.disclaimer)
        btnAccept.text = getString(R.string.accept)

        lifecycleScope.launch {
            val lang = AppPreferences.getLanguage(requireContext())
            isEnglish = lang == "en"
            btnToggle.text = if (isEnglish) "Māori" else "English"
        }

        btnToggle.setOnClickListener {
            isEnglish = !isEnglish
            val newLang = if (isEnglish) "en" else "mi"

            lifecycleScope.launch {
                AppPreferences.setLanguage(requireContext(), newLang)
                requireActivity().recreate()
            }
        }

        btnAccept.setOnClickListener {
            lifecycleScope.launch {
                AppPreferences.setDisclaimerSeen(requireContext())
                findNavController().navigate(R.id.homeScreenFragment)
            }
        }
    }
}
