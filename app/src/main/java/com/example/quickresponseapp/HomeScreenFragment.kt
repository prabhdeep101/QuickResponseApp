package com.example.quickresponseapp

import android.content.Context
import android.os.Bundle
import android.content.res.Resources
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class HomeScreenFragment : Fragment(R.layout.fragment_home) {

    // Apply saved language
    override fun onAttach(context: Context) {
        super.onAttach(AppPreferences.applySavedLocale(context))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adjust navigation drawer to 75% of screen
        val navView = activity?.findViewById<NavigationView>(R.id.nav_view)
        val drawerWidth = (Resources.getSystem().displayMetrics.widthPixels * 0.75).toInt()
        navView?.layoutParams?.width = drawerWidth
        navView?.requestLayout()

        // Navigate to settings screen
        val settingsButton = view.findViewById<ImageButton>(R.id.settings_button)
        settingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_settingsFragment)
        }
        // Navigate to contacts page
        val contactsButton = view.findViewById<MaterialButton>(R.id.contacts_button)
        contactsButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_contactsFragment)
        }
        // Navigate to Quiz
        val QuizButton = view.findViewById<Button>(R.id.talk_to_kauri)
        QuizButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_quizFragment)
        }
        // Navigate to messages
        val messagesButton = view.findViewById<Button>(R.id.message_button)
        messagesButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_messagesFragment)
        }
        // Navigate to emergency call screen
        val EmergencyButton = view.findViewById<ImageButton>(R.id.emergency_button)
        EmergencyButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_emergencyPageFragment)
        }
    }
}
