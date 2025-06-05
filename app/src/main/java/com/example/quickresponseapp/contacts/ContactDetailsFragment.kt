package com.example.quickresponseapp.contacts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.quickresponseapp.R
import com.example.quickresponseapp.databinding.FragmentContactDetailsBinding

class ContactDetailsFragment : Fragment(R.layout.fragment_contact_details) {

    private val args: ContactDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentContactDetailsBinding.bind(view)

        val contact = args.contact

        // Populate fields
        binding.apply {
            contactName.text = contact.name
            contactPhone.text = contact.phone
            contactRelation.text = contact.relation
            contactAddress.text = contact.address

            textOranga.text = if (contact.isOrangaTamarikiApproved) {
                "THIS PERSON IS ORANGA TAMARIKI CERTIFIED"
            } else {
                "NOT ORANGA TAMARIKI CERTIFIED"
            }

            // Similarly for default:
            // You can add another TextView ID if needed
            // Example:
            // contactDefault.text = if (contact.isDefault) "DEFAULT CONTACT" else "NOT DEFAULT"

            // Profile image
            if (contact.profileImageUri != null) {
                imageProfile.setImageURI(android.net.Uri.parse(contact.profileImageUri))
            } else {
                imageProfile.setImageResource(R.drawable.kaurihead)
            }

            // Edit button
            buttonEdit.setOnClickListener {
                val action = ContactDetailsFragmentDirections.actionContactDetailsFragmentToEditContactFragment(contact)
                findNavController().navigate(action)
            }
        }
    }
}