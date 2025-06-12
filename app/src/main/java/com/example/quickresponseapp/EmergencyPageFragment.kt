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
    private val contactsViewModel: ContactsViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private var guardianPhone: String? = null
    private var childName: String = "Your child"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val policeCall = view.findViewById<MaterialButton>(R.id.callPoliceBtn)
        val ambulanceCall = view.findViewById<MaterialButton>(R.id.callAmbulanceBtn)
        val guardianCall = view.findViewById<CircleImageView>(R.id.guardianImage)

        val msgPolice = view.findViewById<MaterialButton>(R.id.kauricallPoliceBtn)
        val msgAmbulance = view.findViewById<MaterialButton>(R.id.kauricallAmbulanceBtn)
        val msgGuardian = view.findViewById<CircleImageView>(R.id.kauriguardianImage)

        val homeButton = view.findViewById<ImageButton>(R.id.home_button)
        val backButton = view.findViewById<TextView>(R.id.back_button)

        // 🔹 Call listeners – work even if profile hasn’t loaded yet
        policeCall.setOnClickListener { dialPhone("111") }
        ambulanceCall.setOnClickListener { dialPhone("111") }
        guardianCall.setOnClickListener { guardianPhone?.let { dialPhone(it) } }

        // 🔹 Message button listeners – always bound
        profileViewModel.getProfile { profile ->
            profile?.let {
                childName = it.name

                // Now set the SMS listeners, after name is loaded
                msgPolice.setOnClickListener { sendEmergencySms("POLICE") }
                msgAmbulance.setOnClickListener { sendEmergencySms("AMBULANCE") }
                msgGuardian.setOnClickListener { sendEmergencySms("GUARDIAN") }
            }
        }

        // 🔹 Navigation
        homeButton.setOnClickListener {
            findNavController().navigate(R.id.homeScreenFragment)
        }

        backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // 🔹 Load guardian contact image and phone
        lifecycleScope.launch {
            val defaultContact: Contact? = contactsViewModel.getDefaultContact()
            guardianPhone = defaultContact?.phone

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

            guardianDrawable?.let {
                guardianCall.setImageDrawable(it)
                msgGuardian.setImageDrawable(it)
            }
        }

        // 🔹 Load child’s name (used in SMS)
        profileViewModel.getProfile { profile ->
            profile?.let {
                childName = it.name
            }
        }
    }

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

    private fun dialPhone(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        startActivity(intent)
    }

    private fun sendSms(phoneNumber: String, message: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("sms:$phoneNumber")
            putExtra("sms_body", message)
        }
        startActivity(intent)
    }
}
