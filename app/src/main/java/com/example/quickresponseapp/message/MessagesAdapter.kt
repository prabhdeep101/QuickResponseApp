package com.example.quickresponseapp.messages

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quickresponseapp.R
import com.example.quickresponseapp.contacts.Contact

// Adapter to display list of contacts in messages screen
class MessagesAdapter(
    private val context: Context,
    private val childName: String,
    private val onMessageClick: (Contact) -> Unit
) : ListAdapter<Contact, MessagesAdapter.ContactViewHolder>(DiffCallback()) {

    // ViewHolder holds views for each contact item
    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.contact_name)
        val defaultLabel: TextView = itemView.findViewById(R.id.text_default)
        val relationText: TextView = itemView.findViewById(R.id.text_title)
        val msgButton: View = itemView.findViewById(R.id.btn_msg)
        val profileImage: ImageView = itemView.findViewById(R.id.image_profile)
    }

    // Used to compared list items
    class DiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }

    // Inflates item view and creates new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact_message, parent, false)
        return ContactViewHolder(view)
    }

    // Binds data
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        // Set contact name and relation
        holder.nameText.text = contact.name
        holder.relationText.text = contact.relation
        // Show default if contact marked as default
        holder.defaultLabel.visibility = if (contact.isDefault) View.VISIBLE else View.GONE

        // When message clicked, launch text app with pre-filled message
        holder.msgButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("sms:${contact.phone}")
                putExtra("sms_body", "Kia Ora, this is Kauri! $childName wants to talk to you!")
            }
            context.startActivity(intent)
        }
    }
}