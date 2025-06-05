package com.example.quickresponseapp.contacts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import androidx.recyclerview.widget.ListAdapter
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
        }

        // Add contact button
        view.findViewById<FloatingActionButton>(R.id.fab_add_contact).setOnClickListener {
            findNavController().navigate(R.id.action_contactsFragment_to_addContactFragment)
        }

        // Emergency call button
        view.findViewById<View>(R.id.emergency_button).setOnClickListener {
            // navigate to emergency call screen
        }
    }

    override fun onItemClick(contact: Contact) {
        // Navigate to contact details fragment
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