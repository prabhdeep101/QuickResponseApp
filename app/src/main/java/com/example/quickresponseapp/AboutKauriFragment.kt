package com.example.quickresponseapp

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutKauriFragment : Fragment(R.layout.about_kauri) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Emergency button click → navigate to emergency screen
        view.findViewById<ImageButton>(R.id.emergency_button).setOnClickListener {
            // Add emergency call nav
        }

        // Add a back button
        view.findViewById<TextView>(R.id.back_button).setOnClickListener {
            findNavController().navigateUp()
        }
    }
}