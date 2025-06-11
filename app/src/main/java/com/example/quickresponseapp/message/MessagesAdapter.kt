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

class MessagesAdapter(
    private val context: Context,
    private val onMessageClick: (Contact) -> Unit
) : ListAdapter<Contact, MessagesAdapter.ContactViewHolder>(DiffCallback()) {

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.contact_name)
        val defaultLabel: TextView = itemView.findViewById(R.id.text_default)
        val relationText: TextView = itemView.findViewById(R.id.text_title)
        val msgButton: View = itemView.findViewById(R.id.btn_msg)
        val profileImage: ImageView = itemView.findViewById(R.id.image_profile)
    }

    class DiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact_message, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        holder.nameText.text = contact.name
        holder.relationText.text = contact.relation
        holder.defaultLabel.visibility = if (contact.isDefault) View.VISIBLE else View.GONE

        holder.msgButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("sms:${contact.phone}")
                putExtra("sms_body", "This is an emergency. Please help me!")
            }
            context.startActivity(intent)
        }
    }
}