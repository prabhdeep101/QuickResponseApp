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
import com.example.todolist.R

class ContactsAdapter(
    private val listener: OnItemClickListener
) : ListAdapter<Contact, ContactsAdapter.ContactViewHolder>(DiffCallback()) {

    inner class ContactViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.image_profile)
        val nameText: TextView = itemView.findViewById(R.id.contact_name)
        val defaultLabel: TextView = itemView.findViewById(R.id.contact_default)
        val relationshipText: TextView = itemView.findViewById(R.id.text_title)
        val callButton: LinearLayout = itemView.findViewById(R.id.btn_call)
        val messageButton: LinearLayout = itemView.findViewById(R.id.btn_msg)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val contact = getItem(position)
                    listener.onItemClick(contact)
                }
            }
            callButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val contact = getItem(position)
                    listener.onCallClick(contact)
                }
            }
            messageButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val contact = getItem(position)
                    listener.onMessageClick(contact)
                }
            }
        }

        fun bind(contact: Contact) {
            nameText.text = contact.name
            relationshipText.text = contact.relation
            defaultLabel.visibility = if (contact.isDefault) View.VISIBLE else View.GONE

            if (contact.profileImageUri != null) {
                // Set custom profile picture
                profileImage.setImageURI(Uri.parse(contact.profileImageUri))
            } else {
                // Set default profile picture
                profileImage.setImageResource(R.drawable.kaurihead)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        holder.bind(contact)
    }

    interface OnItemClickListener {
        fun onItemClick(contact: Contact)
        fun onCallClick(contact: Contact)
        fun onMessageClick(contact: Contact)
    }

    class DiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Contact, newItem: Contact) =
            oldItem == newItem
    }

}