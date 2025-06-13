package com.example.quickresponseapp.contacts

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.quickresponseapp.R
import com.example.quickresponseapp.databinding.FragmentContactDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactDetailsFragment : Fragment(R.layout.fragment_contact_details) {
    // Get arguments from navigation
    private val args: ContactDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Extract contact from arguments
        val contact = args.contact
        // Bind to access user interface elements
        val binding = FragmentContactDetailsBinding.bind(view)

        // Set up showing the contact info
        binding.apply {
            contactName.text = contact.name
            contactAddress.text = contact.address
            contactPhone.text = contact.phone
            contactRelation.text = contact.relation

            // Load contact image or set default image
            val uri = contact.photoUri
            if (!uri.isNullOrEmpty()) {
                try {
                    contactImageView.setImageURI(Uri.parse(uri))
                    if (contactImageView.drawable == null) {
                        contactImageView.setImageResource(R.drawable.kaurihead)
                    }
                } catch (e: Exception) {
                    contactImageView.setImageResource(R.drawable.kaurihead)
                }
            } else {
                contactImageView.setImageResource(R.drawable.kaurihead)
            }

            // Show/Hide labels depending on checks (true or false)
            textOranga.visibility = if (contact.isOrangaTamarikiApproved) View.VISIBLE else View.GONE
            textDefault.visibility = if (contact.isDefault) View.VISIBLE else View.GONE

            // Leads to edit contact screen from edit button
            buttonEdit.setOnClickListener {
                val action = ContactDetailsFragmentDirections
                    .actionContactDetailsFragmentToEditContactFragment2(contact)
                findNavController().navigate(action)
            }

            // Back button to move back to contacts page
            backButton.setOnClickListener {
                findNavController().navigate(R.id.contactsFragment)
            }

            // Access emergency call screen
            emergencyButton.setOnClickListener {
                findNavController().navigate(R.id.action_contactDetailsFragment_to_emergencyPageFragment)
            }
        }
    }
}