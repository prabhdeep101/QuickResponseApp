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
    private val viewModel: ContactsViewModel by viewModels()
    private lateinit var contactsAdapter: ContactsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_contacts)
        contactsAdapter = ContactsAdapter(this)

        recyclerView.apply {
            adapter = contactsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            contactsAdapter.submitList(contacts)

            val emptyPlaceholder = view.findViewById<TextView>(R.id.empty_placeholder)
            emptyPlaceholder.visibility = if (contacts.isEmpty()) View.VISIBLE else View.GONE
        }

        view.findViewById<TextView>(R.id.back_button).setOnClickListener {
            findNavController().navigate(R.id.homeScreenFragment)
        }

        // Add contact button
        view.findViewById<FloatingActionButton>(R.id.fab_add_contact).setOnClickListener {
            findNavController().navigate(R.id.action_contactsFragment_to_addContactFragment)
        }

        // Emergency call button
        view.findViewById<View>(R.id.emergency_button).setOnClickListener {
            findNavController().navigate(R.id.action_contactsFragment_to_emergencyPageFragment)
        }
    }

    override fun onItemClick(contact: Contact) {
        val action = ContactsFragmentDirections.actionContactsFragmentToContactDetailsFragment2(contact)
        findNavController().navigate(action)

    }

    override fun onCallClick(contact: Contact) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:${contact.phone}"))
        startActivity(intent)
    }

    override fun onMessageClick(contact: Contact) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:${contact.phone}"))
        startActivity(intent)
    }
}