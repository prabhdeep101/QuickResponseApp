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

    private val viewModel: MessagesViewModel by viewModels()
    private lateinit var messagesAdapter: MessagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_messages)
        val messageAllButton = view.findViewById<Button>(R.id.message_all_button)
        val backButton = view.findViewById<TextView>(R.id.back_button)
        val emptyPlaceholder = view.findViewById<TextView>(R.id.empty_placeholder)
        val emergencyButton = view.findViewById<ImageButton>(R.id.emergency_button)

        // Load profile data
        lifecycleScope.launch {
            val profileDao = com.example.quickresponseapp.profile.ProfileDatabase
                .getDatabase(requireContext())
                .profileDao()

            val profile = profileDao.getProfile()
            val childName = profile?.name ?: "your child"

            // Initialize adapter with loaded name
            messagesAdapter = MessagesAdapter(requireContext(), childName) { contact ->
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("sms:${contact.phone}")
                    putExtra("sms_body", "Kia Ora, this is Kauri! $childName wants to talk to you!")
                }
                startActivity(intent)
            }

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = messagesAdapter

            viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
                val sorted = contacts.sortedByDescending { it.isDefault }
                messagesAdapter.submitList(sorted)

                emptyPlaceholder.visibility = if (sorted.isEmpty()) View.VISIBLE else View.GONE

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

        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        emergencyButton.setOnClickListener {
            findNavController().navigate(R.id.action_messagesFragment_to_emergencyPageFragment)
        }
    }
}