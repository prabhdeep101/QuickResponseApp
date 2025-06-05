package com.example.quickresponseapp.contacts

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

    private val args: ContactDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contact = args.contact

        val binding = FragmentContactDetailsBinding.bind(view)

        binding.apply {
            contactName.text = contact.name
            contactAddress.text = contact.address
            contactPhone.text = contact.phone
            contactRelation.text = contact.relation

            textOranga.visibility = if (contact.isOrangaTamarikiApproved) View.VISIBLE else View.GONE
            textDefault.visibility = if (contact.isDefault) View.VISIBLE else View.GONE

            // Edit button click
            buttonEdit.setOnClickListener {
                val action = ContactDetailsFragmentDirections.actionContactDetailsFragmentToEditContactFragment2(contact)
                findNavController().navigate(action)
            }

            // Back button click
            backButton.setOnClickListener {
                findNavController().navigateUp()
            }

            emergencyButton.setOnClickListener {
                // Optional: navigate to Emergency or open dialer
            }
        }
    }
}