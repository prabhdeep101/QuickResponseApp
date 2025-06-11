package com.example.quickresponseapp

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.io.File

class EmergencyPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_emergency_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val policeCall = view.findViewById<ImageButton>(R.id.callPoliceBtn)
        val ambulanceCall = view.findViewById<ImageButton>(R.id.callAmbulanceBtn)
        val guardianCall = view.findViewById<ImageButton>(R.id.callGuardianBtn)

        val msgPolice = view.findViewById<ImageButton>(R.id.msgPoliceBtn)
        val msgAmbulance = view.findViewById<ImageButton>(R.id.msgAmbulanceBtn)
        val msgGuardian = view.findViewById<ImageButton>(R.id.msgGuardianBtn)

        // 🔹 Load guardian image from internal storage or fallback to default
        val guardianFile = File(requireContext().filesDir, "guardian_profile.jpg")
        val drawable: Drawable? = if (guardianFile.exists()) {
            Drawable.createFromPath(guardianFile.absolutePath)
        } else {
            ContextCompat.getDrawable(requireContext(), R.drawable.kaurihead)
        }

        // 🔹 Set image to guardianCall button
        guardianCall.setImageDrawable(drawable)

        // 🔹 Emergency phone number or stored one
        val emergencyPhone = "111"
        val guardianPhone = "12345678" // 🔁 Replace with dynamic value if needed
        val emergencyMessage = "This is an emergency. Please help me!"

        // 🔹 Call actions
        policeCall.setOnClickListener {
            dialPhone(emergencyPhone)
        }

        ambulanceCall.setOnClickListener {
            dialPhone(emergencyPhone)
        }

        guardianCall.setOnClickListener {
            dialPhone(guardianPhone)
        }

        // 🔹 Message actions
        msgPolice.setOnClickListener {
            sendSms(emergencyPhone, emergencyMessage)
        }

        msgAmbulance.setOnClickListener {
            sendSms(emergencyPhone, emergencyMessage)
        }

        msgGuardian.setOnClickListener {
            sendSms(guardianPhone, emergencyMessage)
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
