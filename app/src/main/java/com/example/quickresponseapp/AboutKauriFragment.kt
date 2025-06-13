package com.example.quickresponseapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AboutKauriFragment : Fragment(R.layout.about_kauri) {

    // Set the saved locale
    override fun onAttach(context: Context) {
        // Apply saved language preference
        super.onAttach(AppPreferences.applySavedLocale(context))
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Emergency call button
        view.findViewById<ImageButton>(R.id.emergency_button).setOnClickListener {
            findNavController().navigate(R.id.action_aboutKauriFragment_to_emergencyPageFragment)
        }

        // Back button moves back a screen
        view.findViewById<TextView>(R.id.back_button).setOnClickListener {
            findNavController().navigateUp()
        }
    }
}