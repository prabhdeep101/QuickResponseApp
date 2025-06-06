// EmergencyCallFragment.kt
package com.example.quickresponseapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.todolist.R

class EmergencyCallFragment : Fragment(R.layout.fragment_emergency_call) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val endButton: ImageButton = view.findViewById(R.id.endButton)
        endButton.setOnClickListener {
            val intent = Intent(requireContext(), HomeScreen::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}