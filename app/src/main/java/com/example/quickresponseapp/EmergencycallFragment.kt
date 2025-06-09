// EmergencyCallFragment.kt
package com.example.quickresponseapp

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class EmergencyCallFragment : Fragment(R.layout.fragment_emergency_call) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val endButton: ImageButton = view.findViewById(R.id.endButton)
        endButton.setOnClickListener {
            // Navigate back to HomeScreen
            findNavController().navigate(R.id.homeScreenFragment)
        }
    }
}