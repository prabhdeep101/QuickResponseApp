package com.example.quickresponseapp

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.quickresponseapp.contacts.Contact
import com.example.quickresponseapp.contacts.ContactsViewModel
import com.example.quickresponseapp.profile.ProfileViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class EmergencyPageFragment : Fragment(R.layout.fragment_emergency_page) {
    // ViewModels for accessing profile and contacts data
    private val contactsViewModel: ContactsViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()

    // Stores guardians phone number and childs name (profile name)
    private var guardianPhone: String? = null
    private var childName: String = "Your child"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Emergency call buttons
        val policeCall = view.findViewById<MaterialButton>(R.id.callPoliceBtn)
        val ambulanceCall = view.findViewById<MaterialButton>(R.id.callAmbulanceBtn)
        val guardianCall = view.findViewById<CircleImageView>(R.id.guardianImage)
        // Emergency Message buttons
        val msgPolice = view.findViewById<MaterialButton>(R.id.kauricallPoliceBtn)
        val msgAmbulance = view.findViewById<MaterialButton>(R.id.kauricallAmbulanceBtn)
        val msgGuardian = view.findViewById<CircleImageView>(R.id.kauriguardianImage)
        // Navigation buttons
        val homeButton = view.findViewById<ImageButton>(R.id.home_button)
        val backButton = view.findViewById<TextView>(R.id.back_button)

        // Set call button actions
        policeCall.setOnClickListener { dialPhone("111") }
        ambulanceCall.setOnClickListener { dialPhone("111") }
        guardianCall.setOnClickListener { guardianPhone?.let { dialPhone(it) } }

        // Load profile data for message buttons
        profileViewModel.getProfile { profile ->
            profile?.let {
                childName = it.name
                // Set message button actions with childs name
                msgPolice.setOnClickListener { sendEmergencySms("POLICE") }
                msgAmbulance.setOnClickListener { sendEmergencySms("AMBULANCE") }
                msgGuardian.setOnClickListener { sendEmergencySms("GUARDIAN") }
            }
        }

        // Navigate to home screen
        homeButton.setOnClickListener {
            findNavController().navigate(R.id.homeScreenFragment)
        }

        // Navigate to previous page
        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Load guardian/contacts image and phone number
        lifecycleScope.launch {
            val defaultContact: Contact? = contactsViewModel.getDefaultContact()
            guardianPhone = defaultContact?.phone

            // Load custom image or fall back to default
            val guardianDrawable: Drawable? = if (defaultContact?.photoUri.isNullOrEmpty()) {
                ContextCompat.getDrawable(requireContext(), R.drawable.kaurihead)
            } else {
                try {
                    defaultContact?.photoUri?.let {
                        val uri = Uri.parse(it)
                        val stream = requireContext().contentResolver.openInputStream(uri)
                        Drawable.createFromStream(stream, uri.toString())
                    }
                } catch (e: Exception) {
                    ContextCompat.getDrawable(requireContext(), R.drawable.kaurihead)
                }
            }
            // Set contacts image to both buttons
            guardianDrawable?.let {
                guardianCall.setImageDrawable(it)
                msgGuardian.setImageDrawable(it)
            }
        }

        // Load the childs name
        profileViewModel.getProfile { profile ->
            profile?.let {
                childName = it.name
            }
        }
    }

    // Send emergency message to default contact based on type
    private fun sendEmergencySms(type: String) {
        guardianPhone?.let {
            val message = when (type) {
                "POLICE" -> "EMERGENCY: $childName is requesting POLICE and is unsafe to call."
                "AMBULANCE" -> "EMERGENCY: $childName is requesting AMBULANCE and is unsafe to call."
                "GUARDIAN" -> "EMERGENCY: $childName needs to talk."
                else -> "EMERGENCY"
            }
            sendSms(it, message)
        }
    }

    // Opens phone app with matching phone number
    private fun dialPhone(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        startActivity(intent)
    }

    // Opens text app with prefilled number and message
    private fun sendSms(phoneNumber: String, message: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("sms:$phoneNumber")
            putExtra("sms_body", message)
        }
        startActivity(intent)
    }
}
