package com.example.quickresponseapp.contacts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quickresponseapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsFragment : Fragment(R.layout.fragment_contacts), ContactsAdapter.OnItemClickListener {
    // ViewModel that holds contact data
    private val viewModel: ContactsViewModel by viewModels()

    // Adapter for displaying contacts in RecyclerView
    private lateinit var contactsAdapter: ContactsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up recycler view
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_contacts)
        contactsAdapter = ContactsAdapter(this)

        recyclerView.apply {
            adapter = contactsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        // Observe contacts and update user interface
        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            contactsAdapter.submitList(contacts)

            // Show placeholder if list empty
            val emptyPlaceholder = view.findViewById<TextView>(R.id.empty_placeholder)
            emptyPlaceholder.visibility = if (contacts.isEmpty()) View.VISIBLE else View.GONE
        }

        // Navigate back to home screen
        view.findViewById<TextView>(R.id.back_button).setOnClickListener {
            findNavController().navigate(R.id.homeScreenFragment)
        }

        // Navigate to add contact page
        view.findViewById<FloatingActionButton>(R.id.fab_add_contact).setOnClickListener {
            findNavController().navigate(R.id.action_contactsFragment_to_addContactFragment)
        }

        // Navigate to Emergency call screen
        view.findViewById<View>(R.id.emergency_button).setOnClickListener {
            findNavController().navigate(R.id.action_contactsFragment_to_emergencyPageFragment)
        }
    }

    // When contact is clicked, navigate to details screen
    override fun onItemClick(contact: Contact) {
        val action = ContactsFragmentDirections.actionContactsFragmentToContactDetailsFragment2(contact)
        findNavController().navigate(action)

    }

    // When call is clicked, open phone app with contact's number
    override fun onCallClick(contact: Contact) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:${contact.phone}"))
        startActivity(intent)
    }

    // When message is clicked, open text app with contact's number
    override fun onMessageClick(contact: Contact) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:${contact.phone}"))
        startActivity(intent)
    }
}