package com.example.quickresponseapp.contacts

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quickresponseapp.R

// Adapter for showing contacts in RecyclerView
class ContactsAdapter(
    private val listener: OnItemClickListener
) : ListAdapter<Contact, ContactsAdapter.ContactViewHolder>(DiffCallback()) {

    // ViewHolder class for holding views for each contact item
    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.image_profile)
        val nameText: TextView = itemView.findViewById(R.id.contact_name)
        val defaultLabel: TextView = itemView.findViewById(R.id.contact_default)
        val relationshipText: TextView = itemView.findViewById(R.id.text_title)
        val callButton: LinearLayout = itemView.findViewById(R.id.btn_call)
        val messageButton: LinearLayout = itemView.findViewById(R.id.btn_msg)

        init {
            // When the contact card is clicked
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val contact = getItem(position)
                    listener.onItemClick(contact)
                }
            }
            // When the call is clicked
            callButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val contact = getItem(position)
                    listener.onCallClick(contact)
                }
            }
            // When the message button is clicked
            messageButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val contact = getItem(position)
                    listener.onMessageClick(contact)
                }
            }
        }

        // Binds data to the views
        fun bind(contact: Contact) {
            nameText.text = contact.name
            relationshipText.text = contact.relation
            defaultLabel.visibility = if (contact.isDefault) View.VISIBLE else View.GONE

            // Load selected image, otherwise show default image
            val uri = contact.photoUri
            if (!uri.isNullOrEmpty()) {
                try {
                    profileImage.setImageURI(Uri.parse(uri))
                    // Check if it successfully loaded anything
                    if (profileImage.drawable == null) {
                        profileImage.setImageResource(R.drawable.kaurihead)
                    }
                } catch (e: Exception) {
                    profileImage.setImageResource(R.drawable.kaurihead)
                }
            } else {
                profileImage.setImageResource(R.drawable.kaurihead)
            }
        }
    }

    // Inflates the layout and creates a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    // Binds data to ViewHolder
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        holder.bind(contact)
    }

    // Interface for handling clicks from adapter
    interface OnItemClickListener {
        fun onItemClick(contact: Contact)
        fun onCallClick(contact: Contact)
        fun onMessageClick(contact: Contact)
    }

    // DiffUtil for updates when contacts list changes
    class DiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact) =
            oldItem == newItem
    }
}