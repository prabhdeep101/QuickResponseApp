package com.example.quickresponseapp.messages

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quickresponseapp.R
import dagger.hilt.android.AndroidEntryPoint

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

        messagesAdapter = MessagesAdapter(
            requireContext(),
            onMessageClick = { contact ->
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("sms:${contact.phone}")
                    putExtra("sms_body", "Kia Ora, this is Kauri! -Child- wants to talk to you!")
                }
                startActivity(intent)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = messagesAdapter

        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            val sorted = contacts.sortedByDescending { it.isDefault }
            messagesAdapter.submitList(sorted)

            messageAllButton.setOnClickListener {
                for (contact in sorted) {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("sms:${contact.phone}")
                        putExtra("sms_body", "Kia Ora, this is Kauri! -Child- wants to talk to you!")
                    }
                    startActivity(intent)
                }
            }
        }

        backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}