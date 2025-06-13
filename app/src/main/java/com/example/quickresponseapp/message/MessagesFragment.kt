package com.example.quickresponseapp.messages

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quickresponseapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MessagesFragment : Fragment() {
    // ViewModel that provides list of contacts
    private val viewModel: MessagesViewModel by viewModels()
    // Adapter to display contacts in RecyclerView
    private lateinit var messagesAdapter: MessagesAdapter

    // Inflate layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Get references to user interface components
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_messages)
        val messageAllButton = view.findViewById<Button>(R.id.message_all_button)
        val backButton = view.findViewById<TextView>(R.id.back_button)
        val emptyPlaceholder = view.findViewById<TextView>(R.id.empty_placeholder)
        val emergencyButton = view.findViewById<ImageButton>(R.id.emergency_button)

        // Load profile data from database
        lifecycleScope.launch {
            val profileDao = com.example.quickresponseapp.profile.ProfileDatabase
                .getDatabase(requireContext())
                .profileDao()

            val profile = profileDao.getProfile()
            val childName = profile?.name ?: "your child"

            // Initialize adapter with child name for text message
            messagesAdapter = MessagesAdapter(requireContext(), childName) { contact ->
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("sms:${contact.phone}")
                    putExtra("sms_body", "Kia Ora, this is Kauri! $childName wants to talk to you!")
                }
                startActivity(intent)
            }

            // Set up RecyclerView
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = messagesAdapter

            // Observe contact list updates
            viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
                // Sort so default shows at the top
                val sorted = contacts.sortedByDescending { it.isDefault }
                messagesAdapter.submitList(sorted)

                // Show placeholder if list is empty
                emptyPlaceholder.visibility = if (sorted.isEmpty()) View.VISIBLE else View.GONE

                // Set up message all button to send text to all contacts
                messageAllButton.setOnClickListener {
                    for (contact in sorted) {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("sms:${contact.phone}")
                            putExtra("sms_body", "Kia Ora, this is Kauri! $childName wants to talk to you!")
                        }
                        startActivity(intent)
                    }
                }
            }
        }

        // Navigate to previous screen
        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // Navigate to emergency call screen
        emergencyButton.setOnClickListener {
            findNavController().navigate(R.id.action_messagesFragment_to_emergencyPageFragment)
        }
    }
}